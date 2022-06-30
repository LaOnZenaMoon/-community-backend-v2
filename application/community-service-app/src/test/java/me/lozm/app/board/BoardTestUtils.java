package me.lozm.app.board;

import com.github.javafaker.Faker;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import me.lozm.app.board.service.BoardService;
import me.lozm.domain.board.vo.BoardCreateVo;
import me.lozm.domain.board.vo.BoardDetailVo;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.ContentType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardTestUtils {

    public static BoardDetailVo.Response createBoard(BoardService boardService) {
        final Faker faker = new Faker();
        return boardService.createBoard(BoardCreateVo.Request.builder()
                .boardType(BoardType.FREE_CONTENTS)
                .contentType(ContentType.GENERAL)
                .title(faker.book().title())
                .content(faker.lorem().sentence())
                .build());
    }

}
