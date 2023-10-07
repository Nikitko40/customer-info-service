package ru.iskhakov;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import ru.iskhakov.dao.CustomerInfoDaoImpl;
import ru.iskhakov.entity.Customers;
import ru.iskhakov.entity.CustomersStat;
import ru.iskhakov.entity.PurchaseStat;
import ru.iskhakov.entity.Statistic;
import ru.iskhakov.exception.DaoException;
import ru.iskhakov.util.ConnectionManager;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class CustomerInfoDaoImplTest {

    private final CustomerInfoDaoImpl customerInfoDaoImpl = new CustomerInfoDaoImpl();
    private static final String CREATE_DB = "create_db.sql";

    @BeforeAll
    void init() {
        try (Connection connection = ConnectionManager.open();
             Statement statement = connection.createStatement()) {
            statement.execute(readQuery());
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    void shouldFindCustomersWhenCallFindCustomersByLastname() throws DaoException {
        //given
        final String givenLastName = "Sidorov";
        final List<Customers> givenList = new ArrayList<>();
        givenList.add(new Customers("Petr", "Sidorov"));
        //when
        List<Customers> customers = customerInfoDaoImpl.findCustomersByLastname(givenLastName);
        //then
        assertThat(customers).isNotEmpty();
        assertThat(customers).containsAll(givenList);
    }

    @Test
    void shouldReturnEmptyCustomersListWhenNotFoundCustomersByLastname() throws DaoException {
        //given
        final String givenLastName = "Zhuchkin";
        //when
        List<Customers> customers = customerInfoDaoImpl.findCustomersByLastname(givenLastName);
        //then
        assertThat(customers).isEmpty();
    }

    @Test
    void shouldFindCustomersWhenCallFindCustomersOnProductSalesCount() throws DaoException {
        //given
        final String givenProductName = "Bread";
        final int givenNumberOfSales = 1;
        final List<Customers> givenList = new ArrayList<>();
        givenList.add(new Customers("Petr", "Sidorov"));
        //when
        List<Customers> customers = customerInfoDaoImpl.findCustomersOnProductSalesCount(givenProductName, givenNumberOfSales);
        //then
        assertThat(customers).isNotEmpty();
        assertThat(customers).containsAll(givenList);
    }

    @Test
    void shouldReturnEmptyCustomersListWhenNotFoundCustomersOnProductSalesCount() throws DaoException {
        //given
        final String givenProductName = "Bread";
        final int givenNumberOfSales = 5;
        //when
        List<Customers> customers = customerInfoDaoImpl.findCustomersOnProductSalesCount(givenProductName, givenNumberOfSales);
        //then
        assertThat(customers).isEmpty();
    }

    @Test
    void shouldFindCustomersWhenCallFindCustomersOnCostRange() throws DaoException {
        //given
        final BigDecimal givenMinExpenses = BigDecimal.valueOf(220);
        final BigDecimal givenMaxExpenses = BigDecimal.valueOf(250);
        final List<Customers> givenList = new ArrayList<>();
        givenList.add(new Customers("Petr", "Sidorov"));
        //when
        List<Customers> customers = customerInfoDaoImpl.findCustomersOnCostRange(givenMinExpenses, givenMaxExpenses);
        //then
        assertThat(customers).isNotEmpty();
        assertThat(customers).containsAll(givenList);
    }

    @Test
    void shouldReturnEmptyCustomersListWhenNotFoundCustomersOnCostRange() throws DaoException {
        //given
        final BigDecimal givenMinExpenses = BigDecimal.valueOf(1);
        final BigDecimal givenMaxExpenses = BigDecimal.valueOf(100);
        //when
        List<Customers> customers = customerInfoDaoImpl.findCustomersOnCostRange(givenMinExpenses, givenMaxExpenses);
        //then
        assertThat(customers).isEmpty();
    }

    @Test
    void shouldFindCustomersWhenCallFindCustomersWithTheLeastNumberOfPurchases() throws DaoException {
        //given
        final int givenCustomersNumber = 1;
        final List<Customers> givenList = new ArrayList<>();
        givenList.add(new Customers("Anton", "Semenov"));
        //when
        List<Customers> customers = customerInfoDaoImpl.findCustomerWithTheLeastNumberOfPurchases(givenCustomersNumber);
        //then
        assertThat(customers).isNotEmpty();
        assertThat(customers).containsAll(givenList);
    }

    @Test
    void shouldReturnEmptyCustomersListWhenNotFoundCustomersWithTheLeastNumberOfPurchases() throws DaoException {
        //given
        final int givenCustomersNumber = 0;
        //when
        List<Customers> customers = customerInfoDaoImpl.findCustomerWithTheLeastNumberOfPurchases(givenCustomersNumber);
        //then
        assertThat(customers).isEmpty();
    }

    @Test
    void shouldThrowExceptionWhenCallFindCustomersWithTheLeastNumberOfPurchases() {

    }

    @Test
    void shouldFindCustomersWhenCallFindCustomersStatisticsOnGivenDate() {
        //given
        final Date startDate = Date.valueOf("2023-09-13");
        final Date endDate = Date.valueOf("2023-09-14");
        List<PurchaseStat> purchaseStats = new ArrayList<>();
        purchaseStats.add(PurchaseStat.builder()
                .name("Mineral water")
                .expenses(BigDecimal.valueOf(25).setScale(2, RoundingMode.HALF_UP))
                .build());
        List<CustomersStat> customersStats = new ArrayList<>();
        customersStats.add(CustomersStat.builder()
                .name("Petrov Ivan")
                .purchases(purchaseStats)
                .totalExpenses(BigDecimal.valueOf(25).setScale(2, RoundingMode.HALF_UP))
                .build());
        Statistic givenStat = Statistic.builder()
                .totalDays(2)
                .customers(customersStats)
                .totalExpenses(BigDecimal.valueOf(25).setScale(2, RoundingMode.HALF_UP))
                .avgExpenses(BigDecimal.valueOf(25).setScale(2, RoundingMode.HALF_UP))
                .build();
        //when
        Statistic stat = customerInfoDaoImpl.findCustomersStatisticsOnGivenDate(startDate, endDate);
        //then
        assertThat(stat).isNotNull();
        assertThat(stat).isEqualTo(givenStat);
    }

    @Test
    void shouldReturnEmptyCustomersListWhenNotFoundCustomersStatisticsOnGivenDate() {
        //given
        final Date startDate = Date.valueOf("2020-09-13");
        final Date endDate = Date.valueOf("2020-09-14");
        Statistic givenStat = Statistic.builder().build();
        //when
        Statistic stat = customerInfoDaoImpl.findCustomersStatisticsOnGivenDate(startDate, endDate);
        //then
        assertThat(stat).isEqualTo(givenStat);
    }

    private String readQuery() {
        try {
            return Resources.toString(Resources.getResource(CustomerInfoDaoImplTest.CREATE_DB), Charsets.UTF_8);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
