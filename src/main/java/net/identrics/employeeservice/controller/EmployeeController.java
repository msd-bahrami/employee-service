package net.identrics.employeeservice.controller;

import net.identrics.employeeservice.controller.model.PageDto;
import net.identrics.employeeservice.entity.Employee;
import net.identrics.employeeservice.service.EmployeeService;
import net.identrics.employeeservice.service.search.SearchCriteria;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * When '/employees/search' is used for these APIs the HAL browser couldn't detect CRUD APIs and generate csv response.
 */
@RestController
@RequestMapping(EmployeeController.EMPLOYEES_SEARCH_PATH)
public class EmployeeController {

    public static final String CSV_CONTENT_TYPE = "text/csv";
    public static final String EMPLOYEES_SEARCH_PATH = "/api/employees-search";
    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public PageDto<Employee> search(@RequestBody @Validated SearchCriteria searchCriteria) {
        return new PageDto<>(employeeService.search(searchCriteria));
    }

    // TODO: 10/7/2022 A performance issue for huge page size exist
    @PostMapping(produces = {CSV_CONTENT_TYPE})
    public @ResponseBody ResponseEntity<Resource> exportSearch(@RequestBody @Validated SearchCriteria searchCriteria) throws IOException {
        String csvContent = employeeService.exportSearch(searchCriteria);
        ByteArrayResource byteArrayResource = new ByteArrayResource(csvContent.getBytes(StandardCharsets.UTF_8));
        String contentDispositionHeaderValue = "attachment; filename=\"result.csv\"";
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, contentDispositionHeaderValue)
                .contentType(MediaType.parseMediaType(CSV_CONTENT_TYPE))
                .body(byteArrayResource);
    }



}
