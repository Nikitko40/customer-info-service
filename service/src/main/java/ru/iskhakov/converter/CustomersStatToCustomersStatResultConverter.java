package ru.iskhakov.converter;

import ru.iskhakov.entity.CustomersStat;
import ru.iskhakov.serialize.out.CustomersStatResult;

import java.util.stream.Collectors;

public class CustomersStatToCustomersStatResultConverter {

    private final PurchaseStatToPurchaseStatResultConverter converter = new PurchaseStatToPurchaseStatResultConverter();
    public CustomersStatResult convert(CustomersStat source) {
        return CustomersStatResult.builder()
                .name(source.getName())
                .purchases(source.getPurchases().stream()
                        .map(converter::convert)
                        .collect(Collectors.toList()))
                .totalExpenses(source.getTotalExpenses())
                .build();
    }
}
