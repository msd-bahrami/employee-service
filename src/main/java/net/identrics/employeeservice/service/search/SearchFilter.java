package net.identrics.employeeservice.service.search;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
public class SearchFilter {

    @NotBlank
    private String key;

    @NotNull
    @Pattern(regexp = "EQUAL|NOT_EQUAL|START_WITH|LESS_THAN|LESS_THAN_OR_EQUAL|GREATER_THAN|GREATER_THAN_OR_EQUAL")
    private SearchCriteria.Operation operation;

    @NotBlank
    private String value;

}
