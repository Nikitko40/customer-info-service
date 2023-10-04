package ru.iskhakov.entity;

import java.math.BigDecimal;
import java.util.Objects;

public class Products {


    private String productName;
    private BigDecimal price;

    public Products(String productName, BigDecimal price) {
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

    @Override
    public String toString() {
        return "Products{" +
                "productName='" + productName + '\'' +
                ", price=" + price +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Products products = (Products) o;
        return Objects.equals(productName, products.productName) && Objects.equals(price, products.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(productName, price);
    }
}
