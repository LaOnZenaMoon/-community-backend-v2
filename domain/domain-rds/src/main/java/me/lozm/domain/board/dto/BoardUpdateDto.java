package me.lozm.domain.board.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.domain.board.code.BoardType;
import me.lozm.domain.board.code.ContentType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardUpdateDto {

    @Getter
    @NoArgsConstructor
    public static class Request {
        private BoardType boardType;
        private ContentType contentType;
        private String title;
        private String content;
    }

}
