package net.identrics.employeeservice.service.search;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Sort;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
@NoArgsConstructor
public class SearchCriteria {

    private List<SearchFilter> filters;

    private Aggregator aggregator = Aggregator.AND;

    @Min(0)
    private int page = 0;

    @Min(1)
    @NotNull
    private int size;

    private String orderBy;

    private Sort.Direction orderDirection;

    public enum Aggregator {
        OR, AND
    }

    public enum Operation {

        EQUAL(CriteriaBuilder::equal),
        NOT_EQUAL(CriteriaBuilder::notEqual),
        START_WITH((b, k, v) -> b.like(k, b.literal(v + "%"))),
        LESS_THAN(CriteriaBuilder::lessThan),
        LESS_THAN_OR_EQUAL(CriteriaBuilder::lessThanOrEqualTo),
        GREATER_THAN(CriteriaBuilder::greaterThan),
        GREATER_THAN_OR_EQUAL(CriteriaBuilder::greaterThanOrEqualTo);

        private final FilterPredicateFunction function;

        Operation(FilterPredicateFunction function) {
            this.function = function;
        }

        Predicate toPredicate(CriteriaBuilder builder, Root<?> entity, String key, Object value) {
            return function.predicate(builder, entity.get(key), value.toString());
        }

        @FunctionalInterface
        interface FilterPredicateFunction {
            Predicate predicate(CriteriaBuilder criteriaBuilder, Path<String> key, String value);
        }

    }
}
