package ru.iskhakov.entity;

public class Customers {

    private String firstName;
    private String lastName;

    public Customers(String name, String lastName) {
        this.firstName = name;
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
