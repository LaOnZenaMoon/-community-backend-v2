package me.lozm.domain.board.vo;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.ContentType;
import me.lozm.global.model.dto.PageQueryParameters;
import me.lozm.global.model.entity.HierarchicalEntity;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardPageVo {

    @Getter
    @AllArgsConstructor
    public static class Request {
        private final PageQueryParameters pageQueryParameters;
    }

    @Getter
    public static class Element {
        private final Long boardId;
        private final HierarchicalEntity hierarchicalBoard;
        private final BoardType boardType;
        private final ContentType contentType;
        private final Long viewCount;
        private final String title;
        private final String content;

        @QueryProjection
        public Element(Long boardId, HierarchicalEntity hierarchicalBoard, BoardType boardType, ContentType contentType, Long viewCount, String title, String content) {
            this.boardId = boardId;
            this.hierarchicalBoard = hierarchicalBoard;
            this.boardType = boardType;
            this.contentType = contentType;
            this.viewCount = viewCount;
            this.title = title;
            this.content = content;
        }
    }

}
