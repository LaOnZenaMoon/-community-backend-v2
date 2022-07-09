package me.lozm.domain.board.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.global.code.CommentType;
import me.lozm.global.code.HierarchyType;
import me.lozm.global.model.HierarchyRequestAble;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentCreateDto {

    @Getter
    @NoArgsConstructor
    public static class Request implements HierarchyRequestAble {
        private HierarchyType hierarchyType;
        private Long parentId;
        @NotNull
        private CommentType commentType;
        @NotBlank
        private String content;
    }

}
