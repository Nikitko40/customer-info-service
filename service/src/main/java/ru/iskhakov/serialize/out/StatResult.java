package ru.iskhakov.serialize.out;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public class StatResult {

    private ResultType type;
    private Integer totalDays;
    private List<CustomersStatResult> customers;
    private BigDecimal totalExpenses;
    private BigDecimal avgExpenses;

}
