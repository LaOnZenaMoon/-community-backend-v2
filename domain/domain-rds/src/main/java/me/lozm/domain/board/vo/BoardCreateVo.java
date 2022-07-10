package me.lozm.domain.board.vo;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.ContentType;
import me.lozm.global.code.HierarchyType;
import me.lozm.global.model.HierarchyRequestAble;
import org.springframework.util.Assert;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.springframework.util.ObjectUtils.isEmpty;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardCreateVo {

    @Getter
    public static class Request implements HierarchyRequestAble {
        private final HierarchyType hierarchyType;
        private final Long parentId;
        private final BoardType boardType;
        private final ContentType contentType;
        private final String title;
        private final String content;

        @Builder
        public Request(HierarchyType hierarchyType, Long parentId, BoardType boardType, ContentType contentType, String title, String content) {
            Assert.notNull(boardType, "게시판 유형은 null 일 수 없습니다.");
            Assert.notNull(contentType, "게시판 내용 유형은 null 일 수 없습니다.");
            Assert.hasLength(title, "게시판 제목은 비어있을 수 없습니다.");
            Assert.hasLength(content, "게시판 내용은 비어있을 수 없습니다.");
            Assert.isTrue(validateHierarchy(hierarchyType, parentId), "계층 관련된 요청값이 잘못되었습니다.");

            this.hierarchyType = isEmpty(hierarchyType) ? HierarchyType.ORIGIN : hierarchyType;
            this.parentId = parentId;
            this.boardType = boardType;
            this.contentType = contentType;
            this.title = title;
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
