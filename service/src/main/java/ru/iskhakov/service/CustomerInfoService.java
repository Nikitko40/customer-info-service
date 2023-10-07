package ru.iskhakov.service;

import ru.iskhakov.converter.CustomerToCustomerResultConverter;
import ru.iskhakov.converter.StatisticToStatResultConverter;
import ru.iskhakov.dao.CustomerInfoDao;
import ru.iskhakov.dao.CustomerInfoDaoImpl;
import ru.iskhakov.exception.DomainException;
import ru.iskhakov.serialize.ObjectSerializer;
import ru.iskhakov.serialize.in.Criteria;
import ru.iskhakov.serialize.in.Stat;
import ru.iskhakov.serialize.out.ResultType;
import ru.iskhakov.serialize.out.SearchResult;
import ru.iskhakov.serialize.out.SearchResults;
import ru.iskhakov.serialize.out.StatResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class CustomerInfoService {

    private final ObjectSerializer serializer = new ObjectSerializer();
    private final CustomerInfoDao dao = new CustomerInfoDaoImpl();
    private final CustomerToCustomerResultConverter converter = new CustomerToCustomerResultConverter();
    private final StatisticToStatResultConverter converterStat = new StatisticToStatResultConverter();

    public String search(String inputJson) throws Exception {
        String json = trimJson(inputJson);
        List<Criteria> criteriaList;
        try {
            criteriaList = serializer.deserializeSearch(json)
                    .stream()
                    .filter(Objects::nonNull)
                    .collect(Collectors.toList());
        } catch (Exception e) {
            throw new DomainException("Json файл не соответствует заявленной схеме");
        }

        List<SearchResults> results = new ArrayList<>();
        for (Criteria criteria : criteriaList) {
            if (criteria.getLastName() != null) {
                results.add(SearchResults.builder()
                        .criteria(criteria)
                        .results(dao.findCustomersByLastname(criteria.getLastName()).stream()
                                .map(converter::convert)
                                .collect(Collectors.toList()))
                        .build());
            } else if (criteria.getProductName() != null && criteria.getMinTimes() != null) {
                results.add(SearchResults.builder()
                        .criteria(criteria)
                        .results(dao.findCustomersOnProductSalesCount(criteria.getProductName(),
                                        criteria.getMinTimes()).stream()
                                .map(converter::convert)
                                .collect(Collectors.toList()))
                        .build());
            } else if (criteria.getMinExpenses() != null && criteria.getMaxExpenses() != null) {
                results.add(SearchResults.builder()
                        .criteria(criteria)
                        .results(dao.findCustomersOnCostRange(criteria.getMinExpenses(), criteria.getMaxExpenses()).stream()
                                .map(converter::convert)
                                .collect(Collectors.toList()))
                        .build());
            } else if (criteria.getBadCustomers() != null) {
                results.add(SearchResults.builder()
                        .criteria(criteria)
                        .results(dao.findCustomerWithTheLeastNumberOfPurchases(criteria.getBadCustomers()).stream()
                                .map(converter::convert)
                                .collect(Collectors.toList()))
                        .build());
            }
        }
        return serializer.serializeSearch(SearchResult.builder()
                .type(ResultType.SEARCH)
                .results(results)
                .build());
    }

    public String stat(String inputJson) throws DomainException {
        Stat stat = serializer.deserializeStat(inputJson);
        if (stat.getEndDate() != null && stat.getStartDate() != null) {
            StatResult statResult = converterStat.convert(dao.findCustomersStatisticsOnGivenDate(stat.getStartDate(), stat.getEndDate()));
            return serializer.serializeStat(statResult);
        }
        throw new DomainException("Проверьте наличие обязательных критериев для поиска");
    }

    private String trimJson(String json) {
        return json.substring(json.indexOf("["), json.lastIndexOf("}"));
    }
}
