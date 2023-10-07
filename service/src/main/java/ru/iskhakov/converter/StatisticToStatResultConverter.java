package ru.iskhakov.converter;

import ru.iskhakov.entity.Statistic;
import ru.iskhakov.serialize.out.ResultType;
import ru.iskhakov.serialize.out.StatResult;

import java.util.stream.Collectors;

public class StatisticToStatResultConverter {
    private final CustomersStatToCustomersStatResultConverter converter = new CustomersStatToCustomersStatResultConverter();

    public StatResult convert(Statistic source) {
        return StatResult.builder()
                .type(ResultType.STAT)
                .totalDays(source.getTotalDays())
                .customers(source.getCustomers().stream()
                        .map(converter::convert)
                        .collect(Collectors.toList()))
                .avgExpenses(source.getAvgExpenses())
                .totalExpenses(source.getTotalExpenses())
                .build();
    }
}
