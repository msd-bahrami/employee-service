package net.identrics.employeeservice.service.search;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class SearchField {

    private String name;

    private List<SearchCriteria.Operation> applicableOperations;

}
