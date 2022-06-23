package me.lozm.domain.board.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.ContentType;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BoardCreateDto {

    @Getter
    @NoArgsConstructor
    public static class Request {
        @NotNull
        private BoardType boardType;
        @NotNull
        private ContentType contentType;
        @NotBlank
        private String title;
        @NotBlank
        private String content;
    }

}
