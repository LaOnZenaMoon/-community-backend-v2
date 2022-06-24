package me.lozm.domain.board.vo;

import lombok.*;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.ContentType;
import me.lozm.global.model.entity.HierarchicalEntity;
import org.springframework.util.Assert;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardCreateVo {

    @Getter
    public static class Request {
        private final BoardType boardType;
        private final ContentType contentType;
        private final String title;
        private final String content;

        @Builder
        public Request(BoardType boardType, ContentType contentType, String title, String content) {
            Assert.notNull(boardType, "게시판 유형은 null 일 수 없습니다.");
            Assert.notNull(contentType, "게시판 내용 유형은 null 일 수 없습니다.");
            Assert.hasLength(title, "게시판 제목은 비어있을 수 없습니다.");
            Assert.hasLength(content, "게시판 내용은 비어있을 수 없습니다.");

            this.boardType = boardType;
            this.contentType = contentType;
            this.title = title;
            this.content = content;
        }
    }

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
