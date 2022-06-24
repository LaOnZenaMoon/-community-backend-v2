package me.lozm.domain.board.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.ContentType;
import me.lozm.global.model.entity.HierarchicalEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardPageDto {

    @Getter
    @AllArgsConstructor
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class Response {
        private final Long boardId;
        private final HierarchicalEntity hierarchicalBoard;
        private final BoardType boardType;
        private final ContentType contentType;
        private final Long viewCount;
        private final String title;
        private final String content;
    }

}
