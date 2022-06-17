package me.lozm.global.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;
import static org.apache.commons.lang3.ObjectUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isBlank;


@Getter
public class PageQueryParameters {

    private static final int MAX_PAGE_SIZE = 100;
    private final Integer pageNumber;
    private final Integer pageSize;
    private final String pageSort;

    @JsonIgnore
    private final List<Sort.Order> pageSortList;


    public PageQueryParameters(Integer pageNumber, Integer pageSize, String pageSort) {
        this.pageNumber = isEmpty(pageNumber) || pageNumber <= 0 ? 1 : pageNumber;
        this.pageSize = isEmpty(pageSize) || pageSize <= 0 ? 10 : pageSize;
        this.pageSort = pageSort;
        this.pageSortList = convertPageSortList(pageSort);
    }

    public PageRequest getPageRequest() {
        checkPageSize();

        if (isEmpty(pageSortList)) {
            return PageRequest.of(pageNumber - 1, pageSize);
        }

        return PageRequest.of(pageNumber - 1, pageSize, Sort.by(pageSortList));
    }

    private List<Sort.Order> convertPageSortList(String pageSort) {
        List<Sort.Order> resultList = new ArrayList<>();

        if (isBlank(pageSort)) {
            resultList.add(Sort.Order.desc("id"));
            return resultList;
        }

        try {
            String[] splitPageSort = pageSort.split(",");
            for (String pageSortItem : splitPageSort) {

                String[] splitPageSortItem = pageSortItem.split(":");
                final String sortField = convertSortField(splitPageSortItem[0]);
                final Sort.Direction sortDirection = convertSortDirection(splitPageSortItem[1]);

                if (sortDirection.isAscending()) {
                    resultList.add(Sort.Order.asc(sortField));
                } else if (sortDirection.isDescending()) {
                    resultList.add(Sort.Order.desc(sortField));
                }

            }
        } catch (Exception e) {
            throw new IllegalArgumentException(format("페이징 정렬 조건이 잘못되었습니다. 이유: %s", e.getMessage()));
        }

        return resultList;
    }

    private Sort.Direction convertSortDirection(String sortDirection) {
        if (isBlank(sortDirection)) {
            throw new IllegalArgumentException("정렬 방향은 비어있을 수 없습니다.");
        }

        return Sort.Direction.fromString(sortDirection.trim());
    }

    private String convertSortField(String sortField) {
        if (isBlank(sortField)) {
            throw new IllegalArgumentException("정렬 필드는 비어있을 수 없습니다.");
        }

        return sortField.trim();
    }

    private void checkPageSize() {
        if (getPageSize() > MAX_PAGE_SIZE) {
            throw new IllegalArgumentException(format("페이지 크기는 %d 을 초과할 수 없습니다.", MAX_PAGE_SIZE));
        }
    }

}
