package ru.iskhakov.entity;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Getter
@Builder
public class Statistic {

    private Integer totalDays;
    private List<CustomersStat> customers;
    private BigDecimal totalExpenses;
    private BigDecimal avgExpenses;

    @Override
    public String toString() {
        return "Statistic{" +
                "totalDays=" + totalDays +
                ", customers=" + customers +
                ", totalExpenses=" + totalExpenses +
                ", avgExpenses=" + avgExpenses +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Statistic statistic = (Statistic) o;
        return Objects.equals(totalDays, statistic.totalDays) && Objects.equals(customers, statistic.customers) && Objects.equals(totalExpenses, statistic.totalExpenses) && Objects.equals(avgExpenses, statistic.avgExpenses);
    }

    @Override
    public int hashCode() {
        return Objects.hash(totalDays, customers, totalExpenses, avgExpenses);
    }
}
