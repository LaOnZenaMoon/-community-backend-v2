package me.lozm.app.board.controller;

import com.github.javafaker.Faker;
import me.lozm.app.board.service.BoardService;
import me.lozm.domain.board.vo.BoardCreateVo;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.ContentType;
import me.lozm.global.documentation.BaseDocumentationTest;
import me.lozm.global.documentation.DocumentationUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static me.lozm.global.documentation.DocumentationUtils.PREFIX_DATA;
import static me.lozm.global.documentation.DocumentationUtils.PREFIX_PAGE_DATA;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BoardControllerTest extends BaseDocumentationTest {

    @Autowired
    private BoardService boardService;


    @DisplayName("게시글 목록 조회(페이징) 성공")
    @Test
    void getBoards_success() throws Exception {
        // Given
        final Faker faker = new Faker();

        final int boardSize = 77;
        for (int i = 0; i < boardSize; i++) {
            boardService.createBoard(BoardCreateVo.Request.builder()
                    .boardType(BoardType.FREE_CONTENTS)
                    .contentType(ContentType.GENERAL)
                    .title(faker.book().title())
                    .content(faker.lorem().sentence())
                    .build());
        }

        // When
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/boards")
                        .header(HttpHeaders.AUTHORIZATION, DocumentationUtils.getAccessToken())
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(this.documentationHandler.document(
                        DocumentationUtils.getPageParameters(),
                        responseFields(DocumentationUtils.getSuccessDefaultResponse())
                                .andWithPrefix(PREFIX_DATA, DocumentationUtils.getPageFieldDescriptor())
                                .andWithPrefix(PREFIX_PAGE_DATA,
                                        subsectionWithPath("boardId").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                        subsectionWithPath("hierarchicalBoard").type(JsonFieldType.OBJECT).description("계층 게시글").optional(),
                                        subsectionWithPath("hierarchicalBoard.commonParentId").type(JsonFieldType.NUMBER).description("공통 상위 게시글 ID").optional(),
                                        subsectionWithPath("hierarchicalBoard.parentId").type(JsonFieldType.NUMBER).description("상위 게시글 ID").optional(),
                                        subsectionWithPath("hierarchicalBoard.groupOrder").type(JsonFieldType.NUMBER).description("그룹 순서").optional(),
                                        subsectionWithPath("hierarchicalBoard.groupLayer").type(JsonFieldType.NUMBER).description("그룹 레이어").optional(),
                                        subsectionWithPath("boardType").type(JsonFieldType.STRING).description(DocumentationUtils.getAllOfEnumElementNames("게시글 유형", BoardType.class)),
                                        subsectionWithPath("contentType").type(JsonFieldType.STRING).description(DocumentationUtils.getAllOfEnumElementNames("게시글 내용 유형", ContentType.class)),
                                        subsectionWithPath("viewCount").type(JsonFieldType.NUMBER).description("게시글 조회수"),
                                        subsectionWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                        subsectionWithPath("content").type(JsonFieldType.STRING).description("내용")
                                )
                ));
    }

    @DisplayName("게시글 생성 성공")
    @Test
    void createBoard_success() throws Exception {
        // Given
        final String requestBody = "{\n" +
                "  \"boardType\": \"ALL\",\n" +
                "  \"contentType\": \"GENERAL\",\n" +
                "  \"title\": \"첫번째 게시글\",\n" +
                "  \"content\": \"Lorem Ipsum is simply dummy text of the printing and typesetting industry. Lorem Ipsum has been the industry's standard dummy text ever since the 1500s, when an unknown printer took a galley of type and scrambled it to make a type specimen book. It has survived not only five centuries, but also the leap into electronic typesetting, remaining essentially unchanged. It was popularised in the 1960s with the release of Letraset sheets containing Lorem Ipsum passages, and more recently with desktop publishing software like Aldus PageMaker including versions of Lorem Ipsum.\"\n" +
                "}";

        // When
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/boards")
                        .header(HttpHeaders.AUTHORIZATION, DocumentationUtils.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(this.documentationHandler.document(
                        requestFields(
                                fieldWithPath("boardType").type(JsonFieldType.STRING).description(DocumentationUtils.getAllOfEnumElementNames("게시글 유형", BoardType.class)),
                                fieldWithPath("contentType").type(JsonFieldType.STRING).description(DocumentationUtils.getAllOfEnumElementNames("게시글 내용 유형", ContentType.class)),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("제목"),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("내용")
                        ),
                        responseFields(DocumentationUtils.getSuccessDefaultResponse())
                ));
    }

}
