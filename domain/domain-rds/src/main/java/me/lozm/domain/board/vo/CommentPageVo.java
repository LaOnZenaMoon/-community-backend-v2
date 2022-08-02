package me.lozm.domain.board.vo;

import com.querydsl.core.annotations.QueryProjection;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.domain.board.code.CommentType;
import me.lozm.global.model.HierarchyResponseAble;
import me.lozm.global.model.dto.PageQueryParameters;
import org.springframework.util.Assert;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentPageVo {

    @Getter
    public static class Request {
        private final Long boardId;
        private final PageQueryParameters pageQueryParameters;

        public Request(Long boardId, PageQueryParameters pageQueryParameters) {
            Assert.notNull(boardId, "게시글 ID 는 null 일 수 없습니다.");
            Assert.notNull(pageQueryParameters, "PageQueryParameters 는 null 일 수 없습니다.");

            this.boardId = boardId;
            this.pageQueryParameters = pageQueryParameters;
        }
    }

    @Getter
    public static class Element {
        private final Long commentId;
        private final HierarchyResponseAble hierarchy;
        private final CommentType commentType;
        private final String content;

        @QueryProjection
        public Element(Long commentId, HierarchyResponseAble hierarchy, CommentType commentType, String content) {
            this.commentId = commentId;
            this.hierarchy = hierarchy;
            this.commentType = commentType;
            this.content = content;
        }
    }

}
