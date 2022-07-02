package me.lozm.domain.board.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.global.code.CommentType;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CommentUpdateDto {

    @Getter
    @NoArgsConstructor
    public static class Request {
        private CommentType commentType;
        private String content;
    }

}
