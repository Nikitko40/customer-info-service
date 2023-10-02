package ru.iskhakov.dao;

import ru.iskhakov.entity.Customers;
import ru.iskhakov.util.ConnectionManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CustomerInfoDao {

    private static final String FIND_CUSTOMERS_BY_FAMILY_SQL =
            "SELECT customers.first_name, customers.last_name " +
                    "FROM customers " +
                    "WHERE last_name = ?";

    private static final String FIND_CUSTOMERS_ON_PRODUCT_SALES_COUNT =
            "SELECT customers.first_name, customers.last_name as total_purchases " +
                    "FROM Customers " +
                    "INNER JOIN Purchases " +
                    "ON customers.customer_id = Purchases.customer " +
                    "INNER JOIN Products " +
                    "ON Product_id = Purchases.product " +
                    "WHERE Products.product_name = ? " +
                    "GROUP BY customers.first_name, customers.last_name " +
                    "HAVING COUNT(Purchases.customer) >= ?";

    private static final String FIND_CUSTOMERS_ON_PRODUCT_EXPENSES =
            "SELECT Customers.first_name, Customers.last_name, SUM(Products.price) as total_cost " +
                    "FROM Customers " +
                    "JOIN Purchases " +
                    "ON Customers.customer_id = Purchases.customer " +
                    "JOIN Products " +
                    "ON Products.product_id = Purchases.product " +
                    "GROUP BY Customers.first_name, Customers.last_name " +
                    "HAVING SUM(Products.price) BETWEEN ? AND ?";

    private static final String FIND_CUSTOMER_WITH_THE_LEAST_NUMBER_OF_PURCHASES =
            "SELECT Customers.first_name, Customers.last_name, COUNT(Purchases.customer) as total_purchases " +
                    "FROM Customers " +
                    "LEFT JOIN Purchases " +
                    "ON Customers.customer_id = Purchases.customer " +
                    "GROUP BY Customers.first_name, Customers.last_name " +
                    "ORDER BY COUNT(Purchases.customer) " +
                    "LIMIT ?";

//    private static final String FIND_CUSTOMERS_STATISTICS_ON_GIVEN_DATE =
//            "SELECT Customers.first_name, ";

    public List<Customers> findCustomerByLastname(String lastName) {

        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_CUSTOMERS_BY_FAMILY_SQL)) {
            preparedStatement.setString(1, lastName);

            return getCustomers(preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Customers> findCustomersOnProductSalesCount(String productName, BigDecimal numberOfSales) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_CUSTOMERS_ON_PRODUCT_SALES_COUNT)) {
            preparedStatement.setString(1, productName);
            preparedStatement.setBigDecimal(2, numberOfSales);

            return getCustomers(preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public Map<Customers, BigDecimal> findCustomersOnCosRange(BigDecimal minExpenses, BigDecimal maxExpenses) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_CUSTOMERS_ON_PRODUCT_EXPENSES)) {
            preparedStatement.setBigDecimal(1, minExpenses);
            preparedStatement.setBigDecimal(2, maxExpenses);

            ResultSet resultSet = preparedStatement.executeQuery();
            Map<Customers, BigDecimal> customers = new HashMap<>();
            while (resultSet.next()) {
                String firstName = resultSet.getString("first_name");
                String lastName = resultSet.getString("last_name");
                BigDecimal costRange = resultSet.getBigDecimal("price");
                Customers customer = new Customers(firstName, lastName);
                customers.put(customer, costRange);
            }
            return customers;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public List<Customers> find_customer_withTheLeastNumberOfPurchases(int customersNumber) {
        try (Connection connection = ConnectionManager.open();
             PreparedStatement preparedStatement = connection.prepareStatement(FIND_CUSTOMER_WITH_THE_LEAST_NUMBER_OF_PURCHASES)) {
            preparedStatement.setInt(1, customersNumber);

            return getCustomers(preparedStatement);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static List<Customers> getCustomers(PreparedStatement preparedStatement) throws SQLException {
        ResultSet resultSet = preparedStatement.executeQuery();
        List<Customers> customers = new ArrayList<>();
        while (resultSet.next()) {
            String firstName = resultSet.getString("first_name");
            String lastName = resultSet.getString("last_name");
            Customers customer = new Customers(firstName, lastName);
            customers.add(customer);
        }
        return customers;
    }
}
