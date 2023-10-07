package ru.iskhakov.converter;

import ru.iskhakov.entity.PurchaseStat;
import ru.iskhakov.serialize.out.PurchaseStatResult;

public class PurchaseStatToPurchaseStatResultConverter {
    public PurchaseStatResult convert(PurchaseStat source) {
        return PurchaseStatResult.builder()
                .expenses(source.getExpenses())
                .name(source.getName())
                .build();
    }
}
