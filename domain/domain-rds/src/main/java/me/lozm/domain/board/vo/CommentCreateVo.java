package me.lozm.domain.board.vo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.global.code.CommentType;
import org.springframework.util.Assert;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentCreateVo {

    @Getter
    public static class Request {
        private final Long boardId;
        private final CommentType commentType;
        private final String content;

        @Builder
        public Request(Long boardId, CommentType commentType, String content) {
            Assert.notNull(boardId, "게시글 ID 는 null 일 수 없습니다.");
            Assert.notNull(commentType, "댓글 유형은 null 일 수 없습니다.");
            Assert.hasLength(content, "댓글 내용은 비어있을 수 없습니다.");

            this.boardId = boardId;
            this.commentType = commentType;
            this.content = content;
        }
    }

}
