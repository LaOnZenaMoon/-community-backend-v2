package me.lozm.global.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.mysema.commons.lang.Assert;
import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CommonPageResponseDto<T> {
    private final int pageNumber;      
    private final int pageSize;
    private final int numberOfElements;
    private final long totalElements;
    private final int totalPages;
    private final List<T> content;

    public CommonPageResponseDto(final Page<?> page, final List<T> content) {
        Assert.notNull(content, "content 는 null 일 수 없습니다.");

        this.pageSize = page.getSize();
        this.pageNumber = page.getNumber() + 1;
        this.numberOfElements = page.getNumberOfElements();
        this.totalElements = page.getTotalElements();
        this.totalPages = page.getTotalPages();
        this.content = content;
    }

}
