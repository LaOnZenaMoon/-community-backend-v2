package me.lozm.domain.board.vo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.ContentType;
import org.springframework.util.Assert;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardUpdateVo {

    @Getter
    public static class Request {
        private final Long boardId;
        private final BoardType boardType;
        private final ContentType contentType;
        private final String title;
        private final String content;

        @Builder
        public Request(Long boardId, BoardType boardType, ContentType contentType, String title, String content) {
            Assert.notNull(boardId, "게시글 ID 는 null 일 수 없습니다.");

            this.boardId = boardId;
            this.boardType = boardType;
            this.contentType = contentType;
            this.title = title;
            this.content = content;
        }
    }

}
