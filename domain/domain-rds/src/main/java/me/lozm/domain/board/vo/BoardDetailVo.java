package me.lozm.domain.board.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.ContentType;
import me.lozm.global.model.entity.HierarchicalEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardDetailVo {

    @Getter
    @AllArgsConstructor
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
