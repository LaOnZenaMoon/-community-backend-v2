package me.lozm.app.board.service;

import me.lozm.domain.board.entity.Board;
import me.lozm.domain.board.service.BoardHelperService;
import me.lozm.domain.board.service.CommentHelperService;
import me.lozm.domain.board.vo.BoardDetailVo;
import me.lozm.domain.board.vo.CommentDetailVo;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.CommentType;
import me.lozm.global.code.ContentType;
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
            createdCommentList.add(createComment(boardDetailVo.getBoardId(), null, CommentType.GENERAL, commentService));
        }

        // When
        boardService.deleteBoard(boardDetailVo.getBoardId());

        // Then
        assertThrows(IllegalArgumentException.class, () -> boardHelperService.getBoard(boardDetailVo.getBoardId()));
        for (CommentDetailVo.Response commentDetailVo : createdCommentList) {
            assertThrows(IllegalArgumentException.class, () -> commentHelperService.getComment(commentDetailVo.getCommentId()));
        }
    }

}
