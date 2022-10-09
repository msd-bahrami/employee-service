package net.identrics.employeeservice.service;

import com.google.common.collect.ImmutableMap;
import com.opencsv.CSVWriter;
import net.identrics.employeeservice.config.EmployeeServiceProperties;
import net.identrics.employeeservice.entity.Employee;
import net.identrics.employeeservice.repository.EmployeeRepository;
import net.identrics.employeeservice.service.search.AbstractSearchService;
import net.identrics.employeeservice.service.search.SearchCriteria;
import net.identrics.employeeservice.service.search.SearchField;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import static net.identrics.employeeservice.service.search.SearchCriteria.Operation.*;

@Service
@Transactional(readOnly = true)
@EnableConfigurationProperties(EmployeeServiceProperties.class)
public class EmployeeService extends AbstractSearchService<Employee, EmployeeRepository> {
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

    private static final String[] HEADER = {NAME_FIELD_NAME, COMPANY_FIELD_NAME, EDUCATION_FIELD_NAME, SALARY_FIELD_NAME};

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository, EmployeeServiceProperties employeeServiceProperties) {
        super(employeeServiceProperties.getSearch().getDefaultOrderDir());
        this.employeeRepository = employeeRepository;
    }


    @Override
    public EmployeeRepository getRepository() {
        return employeeRepository;
    }

    @Override
    public Map<String, SearchField> getFieldMap() {
        return fieldMap;
    }

    @Transactional
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public String exportSearch(SearchCriteria searchCriteria) throws IOException {
        Page<Employee> result = search(searchCriteria);
        return convertToCsv(result.getContent());
    }

    String convertToCsv(List<Employee> employees) throws IOException {
        String csvResult;
        StringWriter stringWriter = new StringWriter();
        try (CSVWriter writer = new CSVWriter(stringWriter)) {
            writer.writeNext(HEADER);
            employees.forEach(employee -> writer.writeNext(new String[]{employee.getName(), employee.getCompany(),
                    employee.getEducation(), String.valueOf(employee.getSalary())}));
            csvResult = stringWriter.toString();
        }
        return csvResult;
    }
}
