package net.identrics.employeeservice.service.search;

import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@AllArgsConstructor
public class SearchService<E> {

    private final JpaSpecificationExecutor<E> repository;

    private final Map<String, SearchField> fieldMap;

    private final Sort.Direction defaultOrderDirection;

    public Page<E> search(SearchCriteria searchCriteria) {
        Specification<E> specification = mapToSpecification(searchCriteria);
        PageRequest pageRequest = mapToPageRequest(searchCriteria);
        return repository.findAll(specification, pageRequest);
    }

    PageRequest mapToPageRequest(SearchCriteria searchCriteria) {
        Sort.Direction direction = Optional.ofNullable(searchCriteria.getOrderDirection())
                .orElse(defaultOrderDirection);
        Sort order = Optional.ofNullable(searchCriteria.getOrderBy())
                .map(orderBy -> Sort.by(direction, orderBy))
                .orElse(Sort.unsorted());
        return PageRequest.of(searchCriteria.getPage(), searchCriteria.getSize(), order);
    }

    Specification<E> mapToSpecification(SearchCriteria searchCriteria) {
        List<SearchFilter> filters = searchCriteria.getFilters();
        if (CollectionUtils.isEmpty(filters)) {
            return Specification.where(null);
        }

        List<Specification<E>> specifications = buildSpecifications(filters);
        Specification<E> root = Specification.where(specifications.get(0));
        return SearchCriteria.Aggregator.AND.equals(searchCriteria.getAggregator()) ?
                specifications.stream()
                        .skip(1)
                        .reduce(root, Specification::and) :
                specifications.stream()
                        .skip(1)
                        .reduce(root, Specification::or);
    }

    private List<Specification<E>> buildSpecifications(List<SearchFilter> searchFilters) {
        return searchFilters.stream()
                .map(this::buildSpecification)
                .toList();
    }

    private Specification<E> buildSpecification(SearchFilter searchFilter) {
        SearchField searchField = fieldMap.get(searchFilter.getKey());
        if (searchField == null || !searchField.getApplicableOperations().contains(searchFilter.getOperation())) {
            throw new IllegalArgumentException("Field name or its operation is not valid");
        }
        return (root, query, criteriaBuilder) -> searchFilter.getOperation()
                .toPredicate(criteriaBuilder, root, searchFilter.getKey(), searchFilter.getValue());
    }

}
