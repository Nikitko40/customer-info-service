package ru.iskhakov.converter;

import ru.iskhakov.entity.Customers;
import ru.iskhakov.serialize.out.CustomerSearchResult;

public class CustomerToCustomerResultConverter {

    public CustomerSearchResult convert(Customers customer) {
        if (customer == null) {
            return null;
        }
        return CustomerSearchResult.builder()
                .firstName(customer.getFirstName())
                .lastName(customer.getLastName())
                .build();
    }
}
