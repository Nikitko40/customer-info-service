package ru.iskhakov.entity;

import java.math.BigDecimal;

public class Products {

    private long product_id;
    private String productName;
    private BigDecimal price;

    public Products(long product_id, String productName, BigDecimal price) {
        this.product_id = product_id;
        this.productName = productName;
        this.price = price;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
