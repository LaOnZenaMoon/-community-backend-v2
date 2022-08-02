package me.lozm.app.board.service;

import me.lozm.domain.board.code.BoardType;
import me.lozm.domain.board.code.CommentType;
import me.lozm.domain.board.code.ContentType;
import me.lozm.domain.board.entity.Board;
import me.lozm.domain.board.service.BoardHelperService;
import me.lozm.domain.board.service.CommentHelperService;
import me.lozm.domain.board.vo.BoardDetailVo;
import me.lozm.domain.board.vo.CommentDetailVo;
import me.lozm.global.code.HierarchyType;
import me.lozm.global.model.HierarchyResponseAble;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static me.lozm.app.board.TestUtils.createBoard;
import static me.lozm.app.board.TestUtils.createComment;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
class BoardServiceImplTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardHelperService boardHelperService;

    @Autowired
    private CommentService commentService;

    @Autowired
    private CommentHelperService commentHelperService;


    @DisplayName("게시글 상세 조회 > 조회수 순차 증가 성공")
    @Test
    void getBoardDetail_addViewCount_success() throws InterruptedException {
        // Given
        BoardDetailVo.Response boardDetailVo = createBoard(BoardType.NEWS, ContentType.GENERAL, boardService);
        final int viewCount = 22;

        final AtomicInteger successCount = new AtomicInteger();
        final AtomicInteger failCount = new AtomicInteger();

        final CountDownLatch countDownLatch = new CountDownLatch(viewCount);
        final ExecutorService executorService = Executors.newFixedThreadPool(10);

        // When
        for (int i = 0; i < viewCount; i++) {
            try {
                executorService.execute(() -> {
                    boardService.getBoardDetail(boardDetailVo.getBoardId());
                    successCount.getAndIncrement();
                });

            } catch (ObjectOptimisticLockingFailureException oe) {
                System.out.println("ObjectOptimisticLockingFailureException message = " + oe.getMessage());
                failCount.getAndIncrement();

            } catch (Exception e) {
                System.out.println("Exception message = " + e.getMessage());
                failCount.getAndIncrement();
            }

            countDownLatch.countDown();
        }

        countDownLatch.await();
        executorService.awaitTermination(3, TimeUnit.SECONDS);

        // Then
        Board board = boardHelperService.getBoard(boardDetailVo.getBoardId());
        assertEquals(viewCount, board.getViewCount());
        assertEquals(viewCount, successCount.get());
        assertEquals(0, failCount.get());
    }

    @DisplayName("게시글 삭제 성공 > 게시글에 속하는 댓글들도 모두 삭제")
    @Test
    void deleteBoard_when_board_has_comments_success() {
        // Given
        BoardDetailVo.Response boardDetailVo = createBoard(BoardType.NEWS, ContentType.GENERAL, boardService);

        List<CommentDetailVo.Response> createdCommentList = new ArrayList<>();
        final int commentSize = 33;
        for (int i = 0; i < commentSize; i++) {
            createdCommentList.add(createComment(boardDetailVo.getBoardId(), CommentType.GENERAL, commentService));
        }

        // When
        boardService.deleteBoard(boardDetailVo.getBoardId());

        // Then
        assertThrows(IllegalArgumentException.class, () -> boardHelperService.getBoard(boardDetailVo.getBoardId()));
        for (CommentDetailVo.Response commentDetailVo : createdCommentList) {
            assertThrows(IllegalArgumentException.class, () -> commentHelperService.getComment(commentDetailVo.getCommentId()));
        }
    }

    @DisplayName("게시글 생성 성공 > 원글")
    @Test
    void createBoard_hierarchyOrigin_success() {
        // Given
        // When
        BoardDetailVo.Response boardDetailVo = createBoard(BoardType.ALL, ContentType.GENERAL, boardService);

        // Then
        checkHierarchy(boardDetailVo.getHierarchy(), boardDetailVo.getHierarchy().getCommonParentId(), boardDetailVo.getHierarchy().getParentId(), 0, 0);
    }

    @DisplayName("게시글 생성 성공 > 원글에 대한 게시글")
    @Test
    void createBoard_hierarchyReplyForOrigin_success() {
        // Given
        BoardDetailVo.Response boardDetailVo = createBoard(BoardType.ALL, ContentType.GENERAL, boardService);

        // When
        BoardDetailVo.Response replyBoardDetailVo1 = createBoard(HierarchyType.REPLY_FOR_ORIGIN, boardDetailVo.getBoardId(), BoardType.NEWS, ContentType.GENERAL, boardService);
        BoardDetailVo.Response replyBoardDetailVo2 = createBoard(HierarchyType.REPLY_FOR_ORIGIN, boardDetailVo.getBoardId(), BoardType.NEWS, ContentType.GENERAL, boardService);
        BoardDetailVo.Response replyBoardDetailVo3 = createBoard(HierarchyType.REPLY_FOR_ORIGIN, boardDetailVo.getBoardId(), BoardType.NEWS, ContentType.GENERAL, boardService);

        // Then
        checkHierarchy(boardDetailVo.getHierarchy(), boardDetailVo.getBoardId(), boardDetailVo.getBoardId(), 0, 0);
        checkHierarchy(replyBoardDetailVo1.getHierarchy(), boardDetailVo.getBoardId(), boardDetailVo.getBoardId(), 1, 1);
        checkHierarchy(replyBoardDetailVo2.getHierarchy(), boardDetailVo.getBoardId(), boardDetailVo.getBoardId(), 1, 2);
        checkHierarchy(replyBoardDetailVo3.getHierarchy(), boardDetailVo.getBoardId(), boardDetailVo.getBoardId(), 1, 3);
    }

    @DisplayName("게시글 생성 성공 > 답변 게시글 대한 게시글")
    @Test
    void createBoard_hierarchyReplyForReply_success() {
        // Given
        BoardDetailVo.Response replyBoardDetailVo1 = createBoard(BoardType.ALL, ContentType.GENERAL, boardService);
        BoardDetailVo.Response replyBoardDetailVo2 = createBoard(BoardType.ALL, ContentType.GENERAL, boardService);

        // When
        BoardDetailVo.Response replyBoardDetailVo1_1 = createBoard(HierarchyType.REPLY_FOR_ORIGIN, replyBoardDetailVo1.getBoardId(), BoardType.NEWS, ContentType.GENERAL, boardService);

        BoardDetailVo.Response replyBoardDetailVo1_1_1 = createBoard(HierarchyType.REPLY_FOR_REPLY, replyBoardDetailVo1_1.getBoardId(), BoardType.NEWS, ContentType.GENERAL, boardService);
        BoardDetailVo.Response replyBoardDetailVo1_1_2 = createBoard(HierarchyType.REPLY_FOR_REPLY, replyBoardDetailVo1_1.getBoardId(), BoardType.NEWS, ContentType.GENERAL, boardService);
        BoardDetailVo.Response replyBoardDetailVo1_1_3 = createBoard(HierarchyType.REPLY_FOR_REPLY, replyBoardDetailVo1_1.getBoardId(), BoardType.NEWS, ContentType.GENERAL, boardService);

        BoardDetailVo.Response replyBoardDetailVo1_2 = createBoard(HierarchyType.REPLY_FOR_ORIGIN, replyBoardDetailVo1.getBoardId(), BoardType.NEWS, ContentType.GENERAL, boardService);
        BoardDetailVo.Response replyBoardDetailVo1_3 = createBoard(HierarchyType.REPLY_FOR_ORIGIN, replyBoardDetailVo1.getBoardId(), BoardType.NEWS, ContentType.GENERAL, boardService);

        BoardDetailVo.Response replyBoardDetailVo2_1 = createBoard(HierarchyType.REPLY_FOR_ORIGIN, replyBoardDetailVo2.getBoardId(), BoardType.NEWS, ContentType.GENERAL, boardService);
        BoardDetailVo.Response replyBoardDetailVo2_2 = createBoard(HierarchyType.REPLY_FOR_ORIGIN, replyBoardDetailVo2.getBoardId(), BoardType.NEWS, ContentType.GENERAL, boardService);

        // Then
        checkHierarchy(replyBoardDetailVo1.getHierarchy(), replyBoardDetailVo1.getBoardId(), replyBoardDetailVo1.getBoardId(), 0, 0);
        checkHierarchy(replyBoardDetailVo1_1.getHierarchy(), replyBoardDetailVo1.getBoardId(), replyBoardDetailVo1.getBoardId(), 1, 1);
        checkHierarchy(replyBoardDetailVo1_1_1.getHierarchy(), replyBoardDetailVo1.getBoardId(), replyBoardDetailVo1_1.getBoardId(), 2, 2);
        checkHierarchy(replyBoardDetailVo1_1_2.getHierarchy(), replyBoardDetailVo1.getBoardId(), replyBoardDetailVo1_1.getBoardId(), 2, 3);
        checkHierarchy(replyBoardDetailVo1_1_3.getHierarchy(), replyBoardDetailVo1.getBoardId(), replyBoardDetailVo1_1.getBoardId(), 2, 4);
        checkHierarchy(replyBoardDetailVo1_2.getHierarchy(), replyBoardDetailVo1.getBoardId(), replyBoardDetailVo1.getBoardId(), 1, 5);
        checkHierarchy(replyBoardDetailVo1_3.getHierarchy(), replyBoardDetailVo1.getBoardId(), replyBoardDetailVo1.getBoardId(), 1, 6);

        checkHierarchy(replyBoardDetailVo2.getHierarchy(), replyBoardDetailVo2.getBoardId(), replyBoardDetailVo2.getBoardId(), 0, 0);
        checkHierarchy(replyBoardDetailVo2_1.getHierarchy(), replyBoardDetailVo2.getBoardId(), replyBoardDetailVo2.getBoardId(), 1, 1);
        checkHierarchy(replyBoardDetailVo2_2.getHierarchy(), replyBoardDetailVo2.getBoardId(), replyBoardDetailVo2.getBoardId(), 1, 2);
    }

    private void checkHierarchy(HierarchyResponseAble originCommentHierarchy, Long commonParentId, Long parentId, Integer groupLayer, Integer groupOrder) {
        assertEquals(commonParentId, originCommentHierarchy.getCommonParentId());
        assertEquals(parentId, originCommentHierarchy.getParentId());
        assertEquals(groupLayer, originCommentHierarchy.getGroupLayer());
        assertEquals(groupOrder, originCommentHierarchy.getGroupOrder());
    }

}
