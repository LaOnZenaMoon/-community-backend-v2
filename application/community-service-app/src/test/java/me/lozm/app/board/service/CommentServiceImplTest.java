package me.lozm.app.board.service;

import me.lozm.domain.board.repository.BoardRepository;
import me.lozm.domain.board.repository.CommentRepository;
import me.lozm.domain.board.vo.BoardDetailVo;
import me.lozm.domain.board.vo.CommentDetailVo;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.CommentType;
import me.lozm.global.code.ContentType;
import me.lozm.global.code.HierarchyType;
import me.lozm.global.model.HierarchyResponseAble;
import org.junit.jupiter.api.AfterEach;
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
    private BoardRepository boardRepository;

    @Autowired
    private BoardService boardService;

    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentService commentService;


    @AfterEach
    void afterEach() {
        boardRepository.deleteAll();
        commentRepository.deleteAll();
    }

    @DisplayName("댓글 생성 성공 > 원글")
    @Test
    void createComment_hierarchy_origin_success() {
        // Given
        BoardDetailVo.Response boardDetailVo = createBoard(BoardType.ALL, ContentType.GENERAL, boardService);

        // When
        CommentDetailVo.Response commentDetailVo = createComment(boardDetailVo.getBoardId(), CommentType.GENERAL, commentService);

        // Then
        checkHierarchy(commentDetailVo.getHierarchy(), commentDetailVo.getCommentId(), commentDetailVo.getCommentId(), 0, 0);
    }

    @DisplayName("댓글 생성 성공 > 원글에 대한 댓글")
    @Test
    void createComment_hierarchy_reply_for_origin_success() {
        // Given
        BoardDetailVo.Response boardDetailVo = createBoard(BoardType.ALL, ContentType.GENERAL, boardService);

        CommentDetailVo.Response commentDetailVo = createComment(boardDetailVo.getBoardId(), CommentType.GENERAL, commentService);

        // When
        CommentDetailVo.Response replyCommentDetailVo1 = createComment(boardDetailVo.getBoardId(), HierarchyType.REPLY_FOR_ORIGIN, commentDetailVo.getCommentId(), CommentType.GENERAL, commentService);
        CommentDetailVo.Response replyCommentDetailVo2 = createComment(boardDetailVo.getBoardId(), HierarchyType.REPLY_FOR_ORIGIN, commentDetailVo.getCommentId(), CommentType.GENERAL, commentService);
        CommentDetailVo.Response replyCommentDetailVo3 = createComment(boardDetailVo.getBoardId(), HierarchyType.REPLY_FOR_ORIGIN, commentDetailVo.getCommentId(), CommentType.GENERAL, commentService);

        // Then
        checkHierarchy(commentDetailVo.getHierarchy(), commentDetailVo.getCommentId(), commentDetailVo.getCommentId(), 0, 0);
        checkHierarchy(replyCommentDetailVo1.getHierarchy(), commentDetailVo.getCommentId(), commentDetailVo.getCommentId(), 1, 1);
        checkHierarchy(replyCommentDetailVo2.getHierarchy(), commentDetailVo.getCommentId(), commentDetailVo.getCommentId(), 1, 2);
        checkHierarchy(replyCommentDetailVo3.getHierarchy(), commentDetailVo.getCommentId(), commentDetailVo.getCommentId(), 1, 3);
    }

    @DisplayName("댓글 생성 성공 > 댓글에 대한 댓글")
    @Test
    void createComment_hierarchy_reply_for_reply_success() {
        // Given
        BoardDetailVo.Response boardDetailVo = createBoard(BoardType.ALL, ContentType.GENERAL, boardService);

        CommentDetailVo.Response commentDetailVo1 = createComment(boardDetailVo.getBoardId(), CommentType.GENERAL, commentService);
        CommentDetailVo.Response commentDetailVo2 = createComment(boardDetailVo.getBoardId(), CommentType.GENERAL, commentService);

        // When
        CommentDetailVo.Response replyCommentDetailVo1_1 = createComment(boardDetailVo.getBoardId(), HierarchyType.REPLY_FOR_ORIGIN, commentDetailVo1.getCommentId(), CommentType.GENERAL, commentService);

        CommentDetailVo.Response replyCommentDetailVo1_1_1 = createComment(boardDetailVo.getBoardId(), HierarchyType.REPLY_FOR_REPLY, replyCommentDetailVo1_1.getCommentId(), CommentType.GENERAL, commentService);
        CommentDetailVo.Response replyCommentDetailVo1_1_2 = createComment(boardDetailVo.getBoardId(), HierarchyType.REPLY_FOR_REPLY, replyCommentDetailVo1_1.getCommentId(), CommentType.GENERAL, commentService);
        CommentDetailVo.Response replyCommentDetailVo1_1_3 = createComment(boardDetailVo.getBoardId(), HierarchyType.REPLY_FOR_REPLY, replyCommentDetailVo1_1.getCommentId(), CommentType.GENERAL, commentService);

        CommentDetailVo.Response replyCommentDetailVo1_2 = createComment(boardDetailVo.getBoardId(), HierarchyType.REPLY_FOR_ORIGIN, commentDetailVo1.getCommentId(), CommentType.GENERAL, commentService);
        CommentDetailVo.Response replyCommentDetailVo1_3 = createComment(boardDetailVo.getBoardId(), HierarchyType.REPLY_FOR_ORIGIN, commentDetailVo1.getCommentId(), CommentType.GENERAL, commentService);

        CommentDetailVo.Response replyCommentDetailVo2_1 = createComment(boardDetailVo.getBoardId(), HierarchyType.REPLY_FOR_ORIGIN, commentDetailVo2.getCommentId(), CommentType.GENERAL, commentService);
        CommentDetailVo.Response replyCommentDetailVo2_2 = createComment(boardDetailVo.getBoardId(), HierarchyType.REPLY_FOR_ORIGIN, commentDetailVo2.getCommentId(), CommentType.GENERAL, commentService);

        // Then
        checkHierarchy(commentDetailVo1.getHierarchy(), commentDetailVo1.getCommentId(), commentDetailVo1.getCommentId(), 0, 0);
        checkHierarchy(replyCommentDetailVo1_1.getHierarchy(), commentDetailVo1.getCommentId(), commentDetailVo1.getCommentId(), 1, 1);
        checkHierarchy(replyCommentDetailVo1_1_1.getHierarchy(), commentDetailVo1.getCommentId(), replyCommentDetailVo1_1.getCommentId(), 2, 2);
        checkHierarchy(replyCommentDetailVo1_1_2.getHierarchy(), commentDetailVo1.getCommentId(), replyCommentDetailVo1_1.getCommentId(), 2, 3);
        checkHierarchy(replyCommentDetailVo1_1_3.getHierarchy(), commentDetailVo1.getCommentId(), replyCommentDetailVo1_1.getCommentId(), 2, 4);
        checkHierarchy(replyCommentDetailVo1_2.getHierarchy(), commentDetailVo1.getCommentId(), commentDetailVo1.getCommentId(), 1, 5);
        checkHierarchy(replyCommentDetailVo1_3.getHierarchy(), commentDetailVo1.getCommentId(), commentDetailVo1.getCommentId(), 1, 6);

        checkHierarchy(commentDetailVo2.getHierarchy(), commentDetailVo2.getCommentId(), commentDetailVo2.getCommentId(), 0, 0);
        checkHierarchy(replyCommentDetailVo2_1.getHierarchy(), commentDetailVo2.getCommentId(), commentDetailVo2.getCommentId(), 1, 1);
        checkHierarchy(replyCommentDetailVo2_2.getHierarchy(), commentDetailVo2.getCommentId(), commentDetailVo2.getCommentId(), 1, 2);
    }

    private void checkHierarchy(HierarchyResponseAble originCommentHierarchy, Long commonParentId, Long parentId, Integer groupLayer, Integer groupOrder) {
        assertEquals(commonParentId, originCommentHierarchy.getCommonParentId());
        assertEquals(parentId, originCommentHierarchy.getParentId());
        assertEquals(groupLayer, originCommentHierarchy.getGroupLayer());
        assertEquals(groupOrder, originCommentHierarchy.getGroupOrder());
    }

}