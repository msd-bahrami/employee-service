package net.identrics.employeeservice.service;

import net.identrics.employeeservice.TestData;
import net.identrics.employeeservice.config.EmployeeServiceProperties;
import net.identrics.employeeservice.entity.Employee;
import net.identrics.employeeservice.repository.EmployeeRepository;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Answers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.IOException;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class EmployeeServiceTest {

    @InjectMocks
    @Spy
    EmployeeService employeeService;

    @Mock
    EmployeeRepository employeeRepository;

    @Mock(answer = Answers.RETURNS_DEEP_STUBS)
    EmployeeServiceProperties employeeServiceProperties;

    @Mock
    Page<Employee> page;

    @BeforeEach
    void setUp() {
        when(employeeServiceProperties.getSearch().getDefaultOrderDir()).thenReturn(Sort.Direction.ASC);
    }

    @Test
    @DisplayName("Given new employee When save Then should call repository")
    void given_newEmployee_when_save_then_shouldCallRepository() {
        Employee expected = TestData.employee();
        when(employeeRepository.save(any())).thenReturn(expected);

        Employee result = employeeService.save(TestData.employee());

        assertEquals(expected, result);
    }

    @Test
    @DisplayName("When export search Then should return CSV content with headers")
    void when_exportSearch_then_shouldReturnCsvContentWithHeaders() throws IOException {
        Employee employee = TestData.employee();
        when(page.getContent()).thenReturn(List.of(employee));
        doReturn(page).when(employeeService).search(any());

        String result = employeeService.exportSearch(TestData.searchEmptyCriteria());

        assertThat(result, CoreMatchers.containsString(EmployeeService.NAME_FIELD_NAME));
        assertThat(result, CoreMatchers.containsString(EmployeeService.COMPANY_FIELD_NAME));
        assertThat(result, CoreMatchers.containsString(EmployeeService.SALARY_FIELD_NAME));
        assertThat(result, CoreMatchers.containsString(EmployeeService.EDUCATION_FIELD_NAME));
    }

    @Test
    @DisplayName("Given criteria has result When exportSearch Then should return CSV content with correct records")
    void given_criteriaHasResult_when_exportSearch_then_shouldReturnCsvContentCorrectRecords() throws IOException {
        Employee employee = TestData.employee();
        when(page.getContent()).thenReturn(List.of(employee));
        doReturn(page).when(employeeService).search(any());

        String result = employeeService.exportSearch(TestData.searchEmptyCriteria());

        assertThat(result, CoreMatchers.containsString(employee.getName()));
        assertThat(result, CoreMatchers.containsString(employee.getCompany()));
        assertThat(result, CoreMatchers.containsString(String.valueOf(employee.getSalary())));
        assertThat(result, CoreMatchers.containsString(employee.getEducation()));
    }
}