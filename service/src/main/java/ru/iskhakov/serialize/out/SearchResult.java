package ru.iskhakov.serialize.out;

import lombok.Builder;

import java.util.List;

@Builder
public class SearchResult {
    private ResultType type;
    private List<SearchResults> results;
    private String message;
}
