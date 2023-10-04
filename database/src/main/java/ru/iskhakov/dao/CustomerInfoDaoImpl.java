package ru.iskhakov.dao;


import org.jetbrains.annotations.NotNull;
import ru.iskhakov.entity.Customers;
import ru.iskhakov.entity.CustomersStat;
import ru.iskhakov.entity.Products;
import ru.iskhakov.entity.Stat;
import ru.iskhakov.exception.DaoException;
import ru.iskhakov.util.ConnectionManager;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

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
    public List<Customers> findCustomersOnProductSalesCount(@NotNull String productName, @NotNull BigDecimal numberOfSales) throws DaoException {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_CUSTOMERS_ON_PRODUCT_SALES_COUNT_SQL)) {
            preparedStatement.setString(1, productName);
            preparedStatement.setBigDecimal(2, numberOfSales);
            return getCustomers(preparedStatement);
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<Customers> findCustomersOnCostRange(@NotNull BigDecimal minExpenses, @NotNull BigDecimal maxExpenses) throws DaoException {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_CUSTOMERS_ON_PRODUCT_EXPENSES_SQL)) {
            preparedStatement.setBigDecimal(1, minExpenses);
            preparedStatement.setBigDecimal(2, maxExpenses);
            return getCustomers(preparedStatement);
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public List<Customers> findCustomerWithTheLeastNumberOfPurchases(@NotNull int customersNumber) throws DaoException {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_CUSTOMER_WITH_THE_LEAST_NUMBER_OF_PURCHASES_SQL)) {
            preparedStatement.setInt(1, customersNumber);
            return getCustomers(preparedStatement);
        } catch (SQLException e) {
            throw new DaoException(e.getMessage());
        }
    }

    @Override
    public Stat findCustomersStatisticsOnGivenDate(@NotNull Date startDate, @NotNull Date endDate) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_CUSTOMERS_STATISTICS_ON_GIVEN_DATE_SQL)) {
            preparedStatement.setDate(1, startDate);
            preparedStatement.setDate(2, endDate);
            preparedStatement.setDate(3, startDate);
            preparedStatement.setDate(4, endDate);

            ResultSet resultSet = preparedStatement.executeQuery();
            Stat stat = new Stat();
            List<CustomersStat> customersStats = new ArrayList<>();
            List<Products> products = new ArrayList<>();
            int totalDays = 0;
            while (resultSet.next()) {
                CustomersStat customersStat = new CustomersStat();
                String firstName = resultSet.getString(FIRST_NAME);
                String lastName = resultSet.getString(LAST_NAME);
                String product_name = resultSet.getString("product_name");
                BigDecimal expenses = resultSet.getBigDecimal("sum");
                if (totalDays == 0) {
                    totalDays = resultSet.getInt("totalDays");
                }
                customersStat.setFirstName(firstName);
                customersStat.setLastName(lastName);
                products.add(new Products(product_name, expenses));
                customersStat.setProducts(products);
                BigDecimal totalExpenses = products.stream()
                        .map(Products::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);
                BigDecimal averageExpenses = products.stream()
                        .map(Products::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add)
                        .divide(BigDecimal.valueOf(products.size()), 2, RoundingMode.HALF_UP);
                stat.setTotalDays(totalDays);
                stat.setAvgExpenses(averageExpenses);
                stat.setTotalExpenses(totalExpenses);
                customersStats.add(customersStat);
                stat.setCustomers(customersStats);
            }
            return stat;
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

}
