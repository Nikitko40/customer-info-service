package ru.iskhakov.entity;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Getter
@Builder
public class PurchaseStat {
    private String name;
    private BigDecimal expenses;

    @Override
    public String toString() {
        return "PurchaseStat{" +
                "name='" + name + '\'' +
                ", expenses=" + expenses +
                '}';
    }
}
