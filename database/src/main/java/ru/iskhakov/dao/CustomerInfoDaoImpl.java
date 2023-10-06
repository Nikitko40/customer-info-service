package ru.iskhakov.dao;


import org.jetbrains.annotations.NotNull;
import ru.iskhakov.entity.Customers;
import ru.iskhakov.entity.CustomersStat;
import ru.iskhakov.entity.PurchaseStat;
import ru.iskhakov.entity.Statistic;
import ru.iskhakov.exception.DaoException;
import ru.iskhakov.util.ConnectionManager;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class CustomerInfoDaoImpl implements CustomerInfoDao {

    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";

    private static final String FIND_CUSTOMERS_BY_FAMILY_SQL =
            "SELECT customers.first_name, customers.last_name " +
                    "FROM customers " +
                    "WHERE last_name = ?";

    private static final String FIND_CUSTOMERS_ON_PRODUCT_SALES_COUNT_SQL =
            "SELECT customers.first_name, customers.last_name " +
                    "FROM Customers " +
                    "INNER JOIN Purchases " +
                    "ON Customers.persistence_id = Purchases.customer_id " +
                    "INNER JOIN Products " +
                    "ON Products.persistence_id = Purchases.product_id " +
                    "WHERE product_name = ? " +
                    "GROUP BY customers.first_name, customers.last_name " +
                    "HAVING COUNT(Purchases.customer_id) >= ?";

    private static final String FIND_CUSTOMERS_ON_PRODUCT_EXPENSES_SQL =
            "SELECT Customers.first_name, Customers.last_name " +
                    "FROM Customers " +
                    "JOIN Purchases " +
                    "ON Customers.persistence_id = Purchases.customer_id " +
                    "JOIN Products " +
                    "ON Products.persistence_id = Purchases.product_id " +
                    "GROUP BY Customers.first_name, Customers.last_name " +
                    "HAVING SUM(Products.price) BETWEEN ? AND ?";

    private static final String FIND_CUSTOMER_WITH_THE_LEAST_NUMBER_OF_PURCHASES_SQL =
            "SELECT Customers.first_name, Customers.last_name " +
                    "FROM Customers " +
                    "LEFT JOIN Purchases " +
                    "ON Customers.persistence_id = Purchases.customer_id " +
                    "GROUP BY Customers.first_name, Customers.last_name " +
                    "ORDER BY COUNT(Purchases.customer_id) " +
                    "LIMIT ?";

    private static final String FIND_CUSTOMERS_STATISTICS_ON_GIVEN_DATE_SQL =
            "SELECT first_name, " +
                    "last_name, " +
                    "product_name, " +
                    "SUM(price), " +
                    "(SELECT count(table_days) FILTER (WHERE (EXTRACT(isodow FROM table_days::date) NOT IN (6, 7))) " +
                    "FROM generate_series(?::date, ?::date, interval '1 day') g (table_days)) as totalDays " +
                    "FROM (SELECT * " +
                    "FROM (SELECT price, product_name, first_name, last_name, purchase_date " +
                    "FROM Purchases " +
                    "INNER JOIN Customers on purchases.customer_id = customers.persistence_id " +
                    "INNER JOIN Products on Products.persistence_id = purchases.product_id " +
                    "WHERE purchase_date BETWEEN date(?) and date(?)) allBetweendates " +
                    "WHERE extract(DOW FROM allBetweendates.purchase_date) BETWEEN 1 and 5) allBetweendatesWithoutHolydays " +
                    "GROUP BY product_name, first_name, last_name";

    @Override
    public List<Customers> findCustomersByLastname(@NotNull String lastName) throws DaoException {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_CUSTOMERS_BY_FAMILY_SQL)) {
            preparedStatement.setString(1, lastName);
            return getCustomers(preparedStatement);
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<Customers> findCustomersOnProductSalesCount(@NotNull String productName, int numberOfSales) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_CUSTOMERS_ON_PRODUCT_SALES_COUNT_SQL)) {
            preparedStatement.setString(1, productName);
            preparedStatement.setInt(2, numberOfSales);
            return getCustomers(preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customers> findCustomersOnCostRange(@NotNull BigDecimal minExpenses, @NotNull BigDecimal maxExpenses) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_CUSTOMERS_ON_PRODUCT_EXPENSES_SQL)) {
            preparedStatement.setBigDecimal(1, minExpenses);
            preparedStatement.setBigDecimal(2, maxExpenses);
            return getCustomers(preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Customers> findCustomerWithTheLeastNumberOfPurchases(int customersNumber) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_CUSTOMER_WITH_THE_LEAST_NUMBER_OF_PURCHASES_SQL)) {
            preparedStatement.setInt(1, customersNumber);
            return getCustomers(preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Statistic findCustomersStatisticsOnGivenDate(@NotNull Date startDate, @NotNull Date endDate) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_CUSTOMERS_STATISTICS_ON_GIVEN_DATE_SQL)) {
            preparedStatement.setDate(1, convert(startDate));
            preparedStatement.setDate(2, convert(endDate));
            preparedStatement.setDate(3, convert(startDate));
            preparedStatement.setDate(4, convert(endDate));

            ResultSet resultSet = preparedStatement.executeQuery();

            Integer totalDays = null;
            Set<CustomersStat> customers = new HashSet<>();
            Map<String, List<PurchaseStat>> maps = new HashMap<>();
            String name;
            while (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                String productName = resultSet.getString("product_name");
                BigDecimal expenses = resultSet.getBigDecimal("sum");
                if (totalDays == null) {
                    totalDays = resultSet.getInt("totalDays");
                }
                name = lastName + " " + firstName;

                List<PurchaseStat> purchase = maps.get(name);
                if (purchase == null || purchase.isEmpty()) {
                    List<PurchaseStat> newPurchase = new ArrayList<>();
                    newPurchase.add(PurchaseStat.builder()
                            .expenses(expenses)
                            .name(productName)
                            .build());
                    maps.put(name, newPurchase);
                } else {
                    purchase.add(PurchaseStat.builder()
                            .name(productName)
                            .expenses(expenses)
                            .build());
                    maps.put(name, purchase);
                }
                customers.add(CustomersStat.builder().name(name).build());
            }

            if (customers.isEmpty()) {
                return Statistic.builder().build();
            }

            List<CustomersStat> allCustomers = customers.stream()
                    .map(el -> {
                        List<PurchaseStat> purchaseStats = maps.get(el.getName());
                        el.setPurchases(purchaseStats);
                        return el;
                    }).collect(Collectors.toList());

            List<CustomersStat> updateCustomers = allCustomers.stream()
                    .map(el -> {
                        BigDecimal allPurchase = BigDecimal.ZERO;
                        for (PurchaseStat stat : el.getPurchases()) {
                            allPurchase = allPurchase.add(stat.getExpenses());
                        }
                        el.setTotalExpenses(allPurchase);
                        return el;
                    }).collect(Collectors.toList());

            List<BigDecimal> expenses = updateCustomers.stream()
                    .map(p -> p.getTotalExpenses())
                    .collect(Collectors.toList());

            return Statistic.builder()
                    .totalDays(totalDays)
                    .customers(updateCustomers)
                    .totalExpenses(expenses.stream().reduce(BigDecimal.ZERO, BigDecimal::add))
                    .avgExpenses(average(expenses, RoundingMode.CEILING))
                    .build();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Customers> getCustomers(PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Customers> customers = new ArrayList<>();
        while (resultSet.next()) {
            Customers customer = new Customers(resultSet.getString(FIRST_NAME),
                    resultSet.getString(LAST_NAME));
            customers.add(customer);
        }
        return customers;
    }

    public static java.sql.Date convert(java.util.Date date) {
        return new java.sql.Date(date.getTime());
    }

    public BigDecimal average(List<BigDecimal> bigDecimals, RoundingMode roundingMode) {
        BigDecimal sum = bigDecimals.stream()
                .map(Objects::requireNonNull)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        return sum.divide(new BigDecimal(bigDecimals.size()), roundingMode);
    }

    public static void main(String[] args) {
        CustomerInfoDaoImpl customerInfoDao = new CustomerInfoDaoImpl();

    }
}
