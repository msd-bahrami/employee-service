package net.identrics.employeeservice.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.data.domain.Sort;

@Data
@ConfigurationProperties("identrics.employee")
public class EmployeeServiceProperties {

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
