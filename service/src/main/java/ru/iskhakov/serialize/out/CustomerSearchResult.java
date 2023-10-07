package ru.iskhakov.serialize.out;

import lombok.Builder;

@Builder
public class CustomerSearchResult {
    private String lastName;
    private String firstName;
}