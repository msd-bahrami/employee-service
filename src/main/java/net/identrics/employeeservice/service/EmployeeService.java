package net.identrics.employeeservice.service;

import com.opencsv.CSVWriter;
import net.identrics.employeeservice.entity.Employee;
import net.identrics.employeeservice.repository.EmployeeRepository;
import net.identrics.employeeservice.service.search.SearchCriteria;
import net.identrics.employeeservice.service.search.SearchField;
import net.identrics.employeeservice.service.search.SearchService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.io.StringWriter;
import java.util.List;
import java.util.Map;

import static net.identrics.employeeservice.config.EmployeeServiceProperties.*;

@Service
@Transactional(readOnly = true)
public class EmployeeService {

    private static final String[] HEADER = {NAME_FIELD_NAME, COMPANY_FIELD_NAME, EDUCATION_FIELD_NAME, SALARY_FIELD_NAME};

    private final EmployeeRepository employeeRepository;
    private SearchService<Employee> employeeSearchService;


    public EmployeeService(EmployeeRepository employeeRepository, @Qualifier("employeeSearchService") SearchService<Employee> employeeSearchService) {
        this.employeeRepository = employeeRepository;
        this.employeeSearchService = employeeSearchService;
    }


    public Map<String, SearchField> getFieldMap() {
        return fieldMap;
    }

    @Transactional
    public Employee save(Employee employee) {
        return employeeRepository.save(employee);
    }

    public String exportSearch(SearchCriteria searchCriteria) throws IOException {
        Page<Employee> result = employeeSearchService.search(searchCriteria);
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
