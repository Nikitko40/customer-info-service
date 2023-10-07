package ru.iskhakov.dao;

import org.jetbrains.annotations.NotNull;
import ru.iskhakov.entity.Customers;
import ru.iskhakov.entity.Statistic;
import ru.iskhakov.exception.DaoException;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public interface CustomerInfoDao {

    List<Customers> findCustomersByLastname(@NotNull String lastName) throws DaoException;

    List<Customers> findCustomersOnProductSalesCount(@NotNull String productName, @NotNull int numberOfSales);

    List<Customers> findCustomersOnCostRange(@NotNull BigDecimal minExpenses, @NotNull BigDecimal maxExpenses);

    List<Customers> findCustomerWithTheLeastNumberOfPurchases(@NotNull int customersNumber);

    Statistic findCustomersStatisticsOnGivenDate(@NotNull Date startDate, @NotNull Date endDate);
}
