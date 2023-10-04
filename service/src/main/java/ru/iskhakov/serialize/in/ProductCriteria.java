package ru.iskhakov.serialize.in;

import java.math.BigDecimal;

public class ProductCriteria implements Criteria {

    String productName;
    BigDecimal minTimes;

    public String getProductName() {
        return productName;
    }

    public BigDecimal getMinTimes() {
        return minTimes;
    }
}
