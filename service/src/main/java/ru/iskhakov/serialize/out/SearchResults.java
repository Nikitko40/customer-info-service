package ru.iskhakov.serialize.out;

import lombok.Builder;
import ru.iskhakov.serialize.in.Criteria;

import java.util.List;

@Builder
public class SearchResults {
    private Criteria criteria;
    private List<CustomerSearchResult> results;
}
