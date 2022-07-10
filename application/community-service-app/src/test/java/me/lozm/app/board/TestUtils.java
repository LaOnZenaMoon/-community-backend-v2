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
import me.lozm.global.code.HierarchyType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class TestUtils {

    public static BoardDetailVo.Response createBoard(BoardType boardType, ContentType contentType, BoardService boardService) {
        final Faker faker = new Faker();
        return boardService.createBoard(BoardCreateVo.Request.builder()
                .hierarchyType(HierarchyType.ORIGIN)
                .parentId(null)
                .boardType(boardType)
                .contentType(contentType)
                .title(faker.book().title())
                .content(faker.lorem().sentence())
                .build());
    }

    public static BoardDetailVo.Response createBoard(HierarchyType hierarchyType, Long parentId, BoardType boardType, ContentType contentType, BoardService boardService) {
        final Faker faker = new Faker();
        return boardService.createBoard(BoardCreateVo.Request.builder()
                .hierarchyType(hierarchyType)
                .parentId(parentId)
                .boardType(boardType)
                .contentType(contentType)
                .title(faker.book().title())
                .content(faker.lorem().sentence())
                .build());
    }

    public static CommentDetailVo.Response createComment(Long boardId, CommentType commentType, CommentService commentService) {
        final Faker faker = new Faker();
        return commentService.createComment(CommentCreateVo.Request.builder()
                .boardId(boardId)
                .hierarchyType(HierarchyType.ORIGIN)
                .parentId(null)
                .commentType(commentType)
                .content(faker.lorem().sentence())
                .build());
    }

    public static CommentDetailVo.Response createComment(Long boardId, HierarchyType hierarchyType, Long parentId, CommentType commentType, CommentService commentService) {
        final Faker faker = new Faker();
        return commentService.createComment(CommentCreateVo.Request.builder()
                .boardId(boardId)
                .hierarchyType(hierarchyType)
                .parentId(parentId)
                .commentType(commentType)
                .content(faker.lorem().sentence())
                .build());
    }

}
