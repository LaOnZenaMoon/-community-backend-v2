package me.lozm.domain.board.vo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.domain.board.code.CommentType;
import me.lozm.global.code.HierarchyType;
import me.lozm.global.model.HierarchyRequestAble;
import org.springframework.util.Assert;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.springframework.util.ObjectUtils.isEmpty;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentCreateVo {

    @Getter
    public static class Request implements HierarchyRequestAble {
        private final Long boardId;
        private final HierarchyType hierarchyType;
        private final Long parentId;
        private final CommentType commentType;
        private final String content;

        @Builder
        public Request(Long boardId, HierarchyType hierarchyType, Long parentId, CommentType commentType, String content) {
            Assert.notNull(boardId, "게시글 ID 는 null 일 수 없습니다.");
            Assert.notNull(commentType, "댓글 유형은 null 일 수 없습니다.");
            Assert.hasLength(content, "댓글 내용은 비어있을 수 없습니다.");
            Assert.isTrue(validateHierarchy(hierarchyType, parentId), "계층 관련된 요청값이 잘못되었습니다.");

            this.boardId = boardId;
            this.hierarchyType = isEmpty(hierarchyType) ? HierarchyType.ORIGIN : hierarchyType;
            this.parentId = parentId;
            this.commentType = commentType;
            this.content = content;
        }

        private boolean validateHierarchy(HierarchyType hierarchyType, Long parentId) {
            if (hierarchyType == HierarchyType.REPLY_FOR_ORIGIN || hierarchyType == HierarchyType.REPLY_FOR_REPLY) {
                return isNotEmpty(parentId);
            }

            return true;
        }
    }

}
