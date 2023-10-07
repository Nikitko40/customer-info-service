package ru.iskhakov.serialize.out;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public class PurchaseStatResult {
    private String name;
    private BigDecimal expenses;
}
