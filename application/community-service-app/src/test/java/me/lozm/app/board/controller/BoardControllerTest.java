package me.lozm.app.board.controller;

import com.github.javafaker.Faker;
import me.lozm.app.board.service.BoardService;
import me.lozm.domain.board.vo.BoardCreateVo;
import me.lozm.domain.board.vo.BoardDetailVo;
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
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static me.lozm.global.documentation.DocumentationUtils.PREFIX_DATA;
import static me.lozm.global.documentation.DocumentationUtils.PREFIX_PAGE_DATA;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BoardControllerTest extends BaseDocumentationTest {

    @Autowired
    private BoardService boardService;


    @DisplayName("게시글 목록 조회(페이징) 성공")
    @Test
    void getBoards_success() throws Exception {
        // Given
        for (int i = 0; i < 77; i++) {
            createBoard();
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
                                .andWithPrefix(PREFIX_PAGE_DATA, getBoardDetailResponseDto())
                ));
    }

    @DisplayName("게시글 상세 조회 성공")
    @Test
    void getBoardDetail_success() throws Exception {
        // Given
        BoardDetailVo.Response boardDetailVo = createBoard();

        // When
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/boards/{boardId}", boardDetailVo.getBoardId())
                        .header(HttpHeaders.AUTHORIZATION, DocumentationUtils.getAccessToken())
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(this.documentationHandler.document(
                        pathParameters(parameterWithName("boardId").description("게시글 ID")),
                        responseFields(DocumentationUtils.getSuccessDefaultResponse())
                                .andWithPrefix(PREFIX_DATA, getBoardDetailResponseDto())
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
                                .andWithPrefix(PREFIX_DATA, getBoardDetailResponseDto())
                ));
    }

    private BoardDetailVo.Response createBoard() {
        final Faker faker = new Faker();
        BoardDetailVo.Response boardDetailVo = boardService.createBoard(BoardCreateVo.Request.builder()
                .boardType(BoardType.FREE_CONTENTS)
                .contentType(ContentType.GENERAL)
                .title(faker.book().title())
                .content(faker.lorem().sentence())
                .build());
        return boardDetailVo;
    }

    private List<FieldDescriptor> getBoardDetailResponseDto() {
        return List.of(subsectionWithPath("boardId").type(JsonFieldType.NUMBER).description("게시글 ID"),
                subsectionWithPath("hierarchy").type(JsonFieldType.OBJECT).description("계층 게시글").optional(),
                subsectionWithPath("hierarchy.commonParentId").type(JsonFieldType.NUMBER).description("공통 상위 게시글 ID").optional(),
                subsectionWithPath("hierarchy.parentId").type(JsonFieldType.NUMBER).description("상위 게시글 ID").optional(),
                subsectionWithPath("hierarchy.groupOrder").type(JsonFieldType.NUMBER).description("그룹 순서").optional(),
                subsectionWithPath("hierarchy.groupLayer").type(JsonFieldType.NUMBER).description("그룹 레이어").optional(),
                subsectionWithPath("boardType").type(JsonFieldType.STRING).description(DocumentationUtils.getAllOfEnumElementNames("게시글 유형", BoardType.class)),
                subsectionWithPath("contentType").type(JsonFieldType.STRING).description(DocumentationUtils.getAllOfEnumElementNames("게시글 내용 유형", ContentType.class)),
                subsectionWithPath("viewCount").type(JsonFieldType.NUMBER).description("게시글 조회수"),
                subsectionWithPath("title").type(JsonFieldType.STRING).description("제목"),
                subsectionWithPath("content").type(JsonFieldType.STRING).description("내용"));
    }

}
