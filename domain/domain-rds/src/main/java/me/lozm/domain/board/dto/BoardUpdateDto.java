package me.lozm.domain.board.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.ContentType;

import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardUpdateDto {

    @Getter
    @NoArgsConstructor
    public static class Request {
        @NotNull
        private Long boardId;
        private BoardType boardType;
        private ContentType contentType;
        private String title;
        private String content;
    }

}
