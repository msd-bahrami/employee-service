package net.identrics.employeeservice.config;

import com.google.common.collect.ImmutableMap;
import lombok.Data;
import net.identrics.employeeservice.service.search.SearchField;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Sort;

import java.util.List;
import java.util.Map;

import static net.identrics.employeeservice.service.search.SearchCriteria.Operation.*;

@Data
@ConfigurationProperties("identrics.employee")
public class EmployeeServiceProperties {

    public static final String COMPANY_FIELD_NAME = "company";
    public static final String NAME_FIELD_NAME = "name";
    public static final String EDUCATION_FIELD_NAME = "education";
    public static final String SALARY_FIELD_NAME = "salary";

    public static final Map<String, SearchField> fieldMap = ImmutableMap.<String, SearchField>builder()
            .put(NAME_FIELD_NAME, new SearchField(NAME_FIELD_NAME, List.of(EQUAL, NOT_EQUAL, START_WITH)))
            .put(COMPANY_FIELD_NAME, new SearchField(COMPANY_FIELD_NAME, List.of(EQUAL, NOT_EQUAL, START_WITH)))
            .put(EDUCATION_FIELD_NAME, new SearchField(EDUCATION_FIELD_NAME, List.of(EQUAL, NOT_EQUAL, START_WITH)))
            .put(SALARY_FIELD_NAME, new SearchField(SALARY_FIELD_NAME, List.of(LESS_THAN, LESS_THAN_OR_EQUAL, GREATER_THAN, GREATER_THAN_OR_EQUAL)))
            .build();

    private SearchProperties search;
    private ImportProperties importing;

    @Data
    public static class SearchProperties {
        private Sort.Direction defaultOrderDir;
    }

    @Data
    public static class ImportProperties {
        private String watchLocation;
        private long watchPollIntervalSeconds;
        private long watchQuietPeriodSeconds;
    }


}
