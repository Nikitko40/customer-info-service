package ru.iskhakov.serialize.in;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class Criteria {
    private String lastName;
    private Integer badCustomers;
    private BigDecimal minExpenses;
    private BigDecimal maxExpenses;
    private String productName;
    private Integer minTimes;
}
