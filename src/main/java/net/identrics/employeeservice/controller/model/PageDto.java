package net.identrics.employeeservice.controller.model;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PageDto<T> {

    private final List<T> records;
    private final long totalCount;

    public PageDto(Page<T> page) {
        this.records = page.getContent();
        this.totalCount = page.getTotalElements();
    }

}