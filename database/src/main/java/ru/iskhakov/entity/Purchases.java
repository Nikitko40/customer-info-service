package ru.iskhakov.entity;

import java.util.Date;

public class Purchases {

    private long purchase_id;
    private Customers customer;
    private Products product;
    private Date purchaseDate;

    public Purchases(long purchase_id, Customers customer, Products product, Date purchaseDate) {
        this.purchase_id = purchase_id;
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
