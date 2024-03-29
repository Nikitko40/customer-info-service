package ru.iskhakov.entity;

import java.util.Date;

public class Purchases {

    private Customers customer;
    private Products product;
    private Date purchaseDate;

    public Purchases() {
    }

    public Purchases(Products product, Date purchaseDate) {
        this.product = product;
        this.purchaseDate = purchaseDate;
    }

    public Purchases(Customers customer, Products product, Date purchaseDate) {
        this.customer = customer;
        this.product = product;
        this.purchaseDate = purchaseDate;
    }

    public Customers getCustomer() {
        return customer;
    }

    public void setCustomer(Customers customer) {
        this.customer = customer;
    }

    public Products getGoods() {
        return product;
    }

    public void setGoods(Products products) {
        this.product = products;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
