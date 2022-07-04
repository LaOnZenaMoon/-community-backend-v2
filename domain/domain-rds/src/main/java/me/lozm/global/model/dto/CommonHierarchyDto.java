package me.lozm.global.model.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.global.code.HierarchyType;
import me.lozm.global.model.HierarchyRequestAble;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommonHierarchyDto {

    @Getter
    @NoArgsConstructor
    public static class Request implements HierarchyRequestAble {
        private HierarchyType hierarchyType;
        private Long parentId;
    }

}
