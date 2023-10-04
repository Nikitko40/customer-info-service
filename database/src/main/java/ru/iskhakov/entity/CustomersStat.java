package ru.iskhakov.entity;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

public class CustomersStat {

    private String firstName;
    private String lastName;
    private List<Products> products;


    public CustomersStat() {
    }

    public CustomersStat(String firstName, String lastName, List<Products> products) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.products = products;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CustomersStat that = (CustomersStat) o;
        return Objects.equals(firstName, that.firstName) && Objects.equals(lastName, that.lastName) && Objects.equals(products, that.products);
    }

    @Override
    public int hashCode() {
        return Objects.hash(firstName, lastName, products);
    }

    @Override
    public String toString() {
        return "CustomersStat{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", products=" + products +
                '}';
    }
}
