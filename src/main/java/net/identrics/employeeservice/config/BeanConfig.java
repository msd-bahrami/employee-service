package net.identrics.employeeservice.config;

import lombok.AllArgsConstructor;
import net.identrics.employeeservice.entity.Employee;
import net.identrics.employeeservice.repository.EmployeeRepository;
import net.identrics.employeeservice.service.search.SearchService;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@EnableConfigurationProperties(EmployeeServiceProperties.class)
@Configuration
@AllArgsConstructor
public class BeanConfig {

    EmployeeServiceProperties employeeServiceProperties;

    @Bean("employeeSearchService")
    public SearchService<Employee> employeeSearchService(EmployeeRepository employeeRepository) {
        return new SearchService<>(employeeRepository, EmployeeServiceProperties.fieldMap,
                employeeServiceProperties.getSearch().getDefaultOrderDir());
    }
}
