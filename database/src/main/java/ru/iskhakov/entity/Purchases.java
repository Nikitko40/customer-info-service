package ru.iskhakov.entity;

import java.util.Date;

public class Purchases {

    private Customers customers;
    private Goods goods;
    private Date purchaseDate;

    public Purchases(Customers customers, Goods goods, Date purchaseDate) {
        this.customers = customers;
        this.goods = goods;
        this.purchaseDate = purchaseDate;
    }

    public Customers getCustomers() {
        return customers;
    }

    public void setCustomers(Customers customers) {
        this.customers = customers;
    }

    public Goods getGoods() {
        return goods;
    }

    public void setGoods(Goods goods) {
        this.goods = goods;
    }

    public Date getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(Date purchaseDate) {
        this.purchaseDate = purchaseDate;
    }
}
