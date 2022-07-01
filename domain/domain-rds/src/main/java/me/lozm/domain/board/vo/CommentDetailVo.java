package me.lozm.domain.board.vo;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.global.code.CommentType;
import me.lozm.global.model.HierarchyAble;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentDetailVo {

    @Getter
    @AllArgsConstructor
    public static class Response {
        private final Long boardId;
        private final Long commentId;
        private final HierarchyAble hierarchy;
        private final CommentType commentType;
        private final String content;
    }

}
