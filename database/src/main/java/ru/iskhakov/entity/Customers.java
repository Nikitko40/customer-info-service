package ru.iskhakov.entity;

public class Customers {

    private  long customer_id;
    private String firstName;
    private String lastName;

    public Customers(long customer_id, String firstName, String lastName) {
        this.customer_id = customer_id;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Customers(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
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
}
