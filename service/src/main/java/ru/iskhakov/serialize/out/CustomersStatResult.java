package ru.iskhakov.serialize.out;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.List;

@Builder
public class CustomersStatResult {
    private String name;
    List<PurchaseStatResult> purchases;
    private BigDecimal totalExpenses;
}
