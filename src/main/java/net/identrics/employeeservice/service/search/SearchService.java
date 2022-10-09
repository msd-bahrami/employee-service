package net.identrics.employeeservice.service.search;

import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Map;

public interface SearchService<E, R extends PagingAndSortingRepository<E, ?> & JpaSpecificationExecutor<E>> {
    R getRepository();

    Map<String, SearchField> getFieldMap();
}
