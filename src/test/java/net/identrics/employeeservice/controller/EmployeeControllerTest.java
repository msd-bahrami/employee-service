package net.identrics.employeeservice.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import net.identrics.employeeservice.TestData;
import net.identrics.employeeservice.entity.Employee;
import net.identrics.employeeservice.service.EmployeeService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.IOException;
import java.util.List;

import static net.identrics.employeeservice.controller.EmployeeController.CSV_CONTENT_TYPE;
import static net.identrics.employeeservice.controller.EmployeeController.EMPLOYEES_SEARCH_PATH;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = EmployeeController.class)
class EmployeeControllerTest {

    public static final String SAMPLE_CSV = "\"name\",\"company\",\"education\",\"salary\"\n" +
            "\"John\",\"Microsoft\",\"Bachelor\",\"1000\"";
    static ObjectWriter objectWriter;

    @MockBean
    EmployeeService employeeService;

    @Mock
    Page<Employee> page;

    @Autowired
    MockMvc mockMvc;

    @BeforeAll
    static void beforeAll() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        objectWriter = objectMapper.writer().withDefaultPrettyPrinter();
    }


    @Test
    @DisplayName("Given valid criteria When search Then should return result")
    void given_validCriteria_when_search_then_shouldReturnResult() throws Exception {
        Employee expectedEmployee = TestData.employee();
        when(page.getContent()).thenReturn(List.of(expectedEmployee));
        when(employeeService.search(any())).thenReturn(page);

        mockMvc.perform(post(EMPLOYEES_SEARCH_PATH)
                        .content(toJsonString(TestData.searchEmptyCriteria()))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$.records[0].name", is(expectedEmployee.getName())))
                .andExpect(jsonPath("$.records[0].company", is(expectedEmployee.getCompany())))
                .andExpect(jsonPath("$.records[0].education", is(expectedEmployee.getEducation())))
                .andExpect(jsonPath("$.records[0].salary", is(expectedEmployee.getSalary()), Long.class));

    }

    @Test
    void given_validCriteria_when_exportSearch_then_shouldReturnResult() throws Exception {
        Employee expectedEmployee = TestData.employee();
        when(page.getContent()).thenReturn(List.of(expectedEmployee));
        when(employeeService.exportSearch(any())).thenReturn(SAMPLE_CSV);

        mockMvc.perform(post(EMPLOYEES_SEARCH_PATH)
                        .content(toJsonString(TestData.searchEmptyCriteria()))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.parseMediaType(CSV_CONTENT_TYPE)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.header().exists(HttpHeaders.CONTENT_DISPOSITION));

    }

    public static String toJsonString(Object o) throws JsonProcessingException {
        return objectWriter.writeValueAsString(o);
    }
}