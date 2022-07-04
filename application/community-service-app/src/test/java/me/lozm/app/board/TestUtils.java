package me.lozm.app.board;

import com.github.javafaker.Faker;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.lozm.app.board.service.BoardService;
import me.lozm.app.board.service.CommentService;
import me.lozm.domain.board.vo.BoardCreateVo;
import me.lozm.domain.board.vo.BoardDetailVo;
import me.lozm.domain.board.vo.CommentCreateVo;
import me.lozm.domain.board.vo.CommentDetailVo;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.CommentType;
import me.lozm.global.code.ContentType;
import me.lozm.global.model.vo.CommonHierarchyVo;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestUtils {

    public static BoardDetailVo.Response createBoard(BoardType boardType, ContentType contentType, BoardService boardService) {
        final Faker faker = new Faker();
        return boardService.createBoard(BoardCreateVo.Request.builder()
                .boardType(boardType)
                .contentType(contentType)
                .title(faker.book().title())
                .content(faker.lorem().sentence())
                .build());
    }

    public static CommentDetailVo.Response createComment(Long boardId, CommonHierarchyVo.Request hierarchy, CommentType commentType, CommentService commentService) {
        final Faker faker = new Faker();
        return commentService.createComment(CommentCreateVo.Request.builder()
                .boardId(boardId)
                .hierarchy(hierarchy)
                .commentType(commentType)
                .content(faker.lorem().sentence())
                .build());
    }

}
