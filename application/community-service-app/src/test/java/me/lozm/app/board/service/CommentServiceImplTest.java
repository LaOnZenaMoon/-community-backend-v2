package me.lozm.app.board.service;

import me.lozm.domain.board.vo.BoardDetailVo;
import me.lozm.domain.board.vo.CommentDetailVo;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.CommentType;
import me.lozm.global.code.ContentType;
import me.lozm.global.code.HierarchyType;
import me.lozm.global.model.HierarchyResponseAble;
import me.lozm.global.model.vo.CommonHierarchyVo;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static me.lozm.app.board.TestUtils.createBoard;
import static me.lozm.app.board.TestUtils.createComment;
import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class CommentServiceImplTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private CommentService commentService;

    @DisplayName("댓글 생성 성공 > 원글")
    @Test
    void createComment_hierarchy_origin_success() {
        // Given
        BoardDetailVo.Response boardDetailVo = createBoard(BoardType.ALL, ContentType.GENERAL, boardService);

        // When
        CommentDetailVo.Response commentDetailVo = createComment(boardDetailVo.getBoardId(), null, CommentType.GENERAL, commentService);

        // Then
        final Long originCommentId = commentDetailVo.getCommentId();
        final HierarchyResponseAble originCommentHierarchy = commentDetailVo.getHierarchy();

        assertEquals(originCommentId, originCommentHierarchy.getCommonParentId());
        assertEquals(originCommentId, originCommentHierarchy.getParentId());
        assertEquals(0, originCommentHierarchy.getGroupLayer());
        assertEquals(0, originCommentHierarchy.getGroupOrder());
    }

    @DisplayName("댓글 생성 성공 > 원글에 대한 댓글")
    @Test
    void createComment_hierarchy_reply_for_origin_success() {
        // Given
        BoardDetailVo.Response boardDetailVo = createBoard(BoardType.ALL, ContentType.GENERAL, boardService);

        CommentDetailVo.Response commentDetailVo = createComment(boardDetailVo.getBoardId(), null, CommentType.GENERAL, commentService);

        // When
        CommonHierarchyVo.Request commonHierarchyRequestVo = new CommonHierarchyVo.Request(HierarchyType.REPLY_FOR_ORIGIN, commentDetailVo.getCommentId());
        CommentDetailVo.Response replyCommentDetailVo1 = createComment(boardDetailVo.getBoardId(), commonHierarchyRequestVo, CommentType.GENERAL, commentService);
        CommentDetailVo.Response replyCommentDetailVo2 = createComment(boardDetailVo.getBoardId(), commonHierarchyRequestVo, CommentType.GENERAL, commentService);
        CommentDetailVo.Response replyCommentDetailVo3 = createComment(boardDetailVo.getBoardId(), commonHierarchyRequestVo, CommentType.GENERAL, commentService);

        // Then
        final Long originCommentId = commentDetailVo.getCommentId();
        final HierarchyResponseAble originCommentHierarchy = commentDetailVo.getHierarchy();

        assertEquals(originCommentId, originCommentHierarchy.getCommonParentId());
        assertEquals(originCommentId, originCommentHierarchy.getParentId());
        assertEquals(0, originCommentHierarchy.getGroupLayer());
        assertEquals(0, originCommentHierarchy.getGroupOrder());

        final HierarchyResponseAble replyCommentHierarchy1 = replyCommentDetailVo1.getHierarchy();

        assertEquals(originCommentId, replyCommentHierarchy1.getCommonParentId());
        assertEquals(originCommentId, replyCommentHierarchy1.getParentId());
        assertEquals(1, replyCommentHierarchy1.getGroupLayer());
        assertEquals(1, replyCommentHierarchy1.getGroupOrder());

        final HierarchyResponseAble replyCommentHierarchy2 = replyCommentDetailVo2.getHierarchy();

        assertEquals(originCommentId, replyCommentHierarchy2.getCommonParentId());
        assertEquals(originCommentId, replyCommentHierarchy2.getParentId());
        assertEquals(1, replyCommentHierarchy2.getGroupLayer());
        assertEquals(2, replyCommentHierarchy2.getGroupOrder());

        final HierarchyResponseAble replyCommentHierarchy3 = replyCommentDetailVo3.getHierarchy();

        assertEquals(originCommentId, replyCommentHierarchy3.getCommonParentId());
        assertEquals(originCommentId, replyCommentHierarchy3.getParentId());
        assertEquals(1, replyCommentHierarchy3.getGroupLayer());
        assertEquals(3, replyCommentHierarchy3.getGroupOrder());
    }

}