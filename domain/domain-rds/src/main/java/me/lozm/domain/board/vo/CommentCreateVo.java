package me.lozm.domain.board.vo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.global.code.CommentType;
import me.lozm.global.model.HierarchyRequestAble;
import me.lozm.global.model.vo.CommonHierarchyVo;
import org.springframework.util.Assert;

import static org.springframework.util.ObjectUtils.isEmpty;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentCreateVo {

    @Getter
    public static class Request {
        private final Long boardId;
        private final CommonHierarchyVo.Request hierarchy;
        private final CommentType commentType;
        private final String content;

        @Builder
        public Request(Long boardId, HierarchyRequestAble hierarchy, CommentType commentType, String content) {
            Assert.notNull(boardId, "게시글 ID 는 null 일 수 없습니다.");
            Assert.notNull(commentType, "댓글 유형은 null 일 수 없습니다.");
            Assert.hasLength(content, "댓글 내용은 비어있을 수 없습니다.");

            this.boardId = boardId;
            this.hierarchy = isEmpty(hierarchy) ? new CommonHierarchyVo.Request() :
                    new CommonHierarchyVo.Request(hierarchy.getHierarchyType(), hierarchy.getParentId());
            this.commentType = commentType;
            this.content = content;
        }
    }

}
