package net.identrics.employeeservice.service.search;

import net.identrics.employeeservice.TestData;
import net.identrics.employeeservice.repository.EmployeeRepository;
import net.identrics.employeeservice.service.EmployeeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.mockito.Answers;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@DataJpaTest
class AbstractSearchServiceTest {

    public static final String NAME_PREFIX = "name ";
    public static final String COMPANY_PREFIX = "company ";
    public static final String EDUCATION_PREFIX = "education ";
    public static final long SALARY_UNIT = 1000L;
    @Mock(answer = Answers.CALLS_REAL_METHODS)
    AbstractSearchService searchService;

    @Autowired
    EmployeeRepository repository;


    @BeforeEach
    void setUp() {
        when(searchService.getRepository()).thenReturn(repository);
        when(searchService.getFieldMap()).thenReturn(EmployeeService.fieldMap);

        repository.saveAll(IntStream.range(0, 11)
                .mapToObj(i -> TestData.employee(NAME_PREFIX + i, COMPANY_PREFIX + i, EDUCATION_PREFIX + i, i * SALARY_UNIT))
                .toList());
    }

    @ParameterizedTest
    @EnumSource(SearchCriteria.Aggregator.class)
    @DisplayName("Given search criteria with different aggregator When search Then should return correct records")
    void given_searchCriteriaWithDifferentAggregator_when_search_then_shouldReturnCorrectRecords(SearchCriteria.Aggregator aggregator) {
        List<SearchFilter> filters = List.of(TestData.searchFilter(EmployeeService.NAME_FIELD_NAME, SearchCriteria.Operation.EQUAL, NAME_PREFIX + 1),
                TestData.searchFilter(EmployeeService.NAME_FIELD_NAME, SearchCriteria.Operation.EQUAL, NAME_PREFIX + 2));

        Page result = searchService.search(TestData.searchCriteria(aggregator, filters));

        assertEquals(aggregator == SearchCriteria.Aggregator.AND ? 0 : 2, result.getTotalElements());

    }

    @ParameterizedTest
    @CsvSource({
            "EQUAL, 1",
            "NOT_EQUAL, 10",
            "START_WITH, 2",
    }
    )
    @DisplayName("Given search criteria with different string operations When search Then should return correct records")
    void given_searchCriteriaWithDifferentStringOperations_when_search_then_shouldReturnCorrectRecords(String operationName, long expectedCount) {
        List<SearchFilter> filters = List.of(TestData.searchFilter(EmployeeService.NAME_FIELD_NAME, SearchCriteria.Operation.valueOf(operationName), NAME_PREFIX + 1));

        Page result = searchService.search(TestData.searchCriteria(SearchCriteria.Aggregator.AND, filters));

        assertEquals(expectedCount, result.getTotalElements());
    }

    @ParameterizedTest
    @CsvSource({
            "LESS_THAN, 4",
            "LESS_THAN_OR_EQUAL, 5",
            "GREATER_THAN, 6",
            "GREATER_THAN_OR_EQUAL, 7",
    }
    )
    @DisplayName("Given search criteria with different numeric operations When search Then should return correct records")
    void given_searchCriteriaWithDifferentNumericOperations_when_search_then_shouldReturnCorrectRecords(String operationName, long expectedCount) {
        List<SearchFilter> filters = List.of(TestData.searchFilter(EmployeeService.SALARY_FIELD_NAME, SearchCriteria.Operation.valueOf(operationName), "4000"));

        Page result = searchService.search(TestData.searchCriteria(SearchCriteria.Aggregator.AND, filters));

        assertEquals(expectedCount, result.getTotalElements());
    }


    @Test
    @DisplayName("Given search criteria with empty filter When search Then should return correct records")
    void given_searchCriteriaWithEmptyFilter_when_search_then_shouldReturnAllRecords() {
        List<SearchFilter> filters = List.of();

        Page result = searchService.search(TestData.searchCriteria(SearchCriteria.Aggregator.AND, filters));

        assertEquals(11, result.getTotalElements());
    }


    @Test
    @DisplayName("Given search criteria with wrong operation When search Then should throw exception")
    void given_searchCriteriaWithWrongOperation_when_search_then_shouldReturnAllRecords() {
        List<SearchFilter> filters = List.of(TestData.searchFilter(EmployeeService.SALARY_FIELD_NAME, SearchCriteria.Operation.START_WITH, "4000"));
        SearchCriteria searchCriteria = TestData.searchCriteria(SearchCriteria.Aggregator.AND, filters);

        assertThrows(IllegalArgumentException.class, () -> searchService.search(searchCriteria));
    }


    @Test
    @DisplayName("Given search criteria with wrong field name When search Then should throw exception")
    void given_searchCriteriaWithWrongFieldName_when_search_then_shouldReturnAllRecords() {
        List<SearchFilter> filters = List.of(TestData.searchFilter("Wrong Field Name", SearchCriteria.Operation.START_WITH, "4000"));
        SearchCriteria searchCriteria = TestData.searchCriteria(SearchCriteria.Aggregator.AND, filters);

        assertThrows(IllegalArgumentException.class, () -> searchService.search(searchCriteria));
    }

}