package net.identrics.employeeservice;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import net.identrics.employeeservice.entity.Employee;
import net.identrics.employeeservice.service.search.SearchCriteria;
import net.identrics.employeeservice.service.search.SearchFilter;

import java.util.List;
import java.util.stream.IntStream;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestData {

    public static final String ANY_NAME = "mohammad";
    public static final String ANY_COMPANY = "identrics";
    public static final String ANY_EDUCATION = "CS";
    public static final long ANY_SALARY = 5000L;
    public static final int ANY_PAGE = 2;
    public static final int ANY_SIZE = 10;
    public static final SearchCriteria.Aggregator ANY_AGGREGATOR = SearchCriteria.Aggregator.AND;


    public static SearchCriteria searchCriteria(SearchCriteria.Aggregator aggregator, List<SearchFilter> filters) {
        SearchCriteria searchCriteria = new SearchCriteria();
        searchCriteria.setFilters(filters);
        searchCriteria.setAggregator(aggregator);
        searchCriteria.setPage(ANY_PAGE);
        searchCriteria.setSize(ANY_SIZE);
        return searchCriteria;
    }

    public static SearchCriteria searchEmptyCriteria() {
        return searchCriteria(ANY_AGGREGATOR, List.of());
    }

        public static SearchFilter searchFilter(String key, SearchCriteria.Operation operation, String value) {
        SearchFilter searchFilter = new SearchFilter();
        searchFilter.setKey(key);
        searchFilter.setOperation(operation);
        searchFilter.setValue(value);
        return searchFilter;
    }

    public static List<SearchFilter> searchFilterList(String key, SearchCriteria.Operation operation, String value, int numberOfRepeat) {
        return IntStream.range(0, numberOfRepeat)
                .mapToObj(i -> searchFilter(key, operation, value))
                .toList();
    }

    public static Employee employee(String name, String company, String education, long salary) {
        Employee employee = new Employee();
        employee.setName(name);
        employee.setCompany(company);
        employee.setEducation(education);
        employee.setSalary(salary);
        return employee;
    }

    public static Employee employee() {
        Employee employee = new Employee();
        employee.setName(ANY_NAME);
        employee.setCompany(ANY_COMPANY);
        employee.setEducation(ANY_EDUCATION);
        employee.setSalary(ANY_SALARY);
        return employee;
    }
}
