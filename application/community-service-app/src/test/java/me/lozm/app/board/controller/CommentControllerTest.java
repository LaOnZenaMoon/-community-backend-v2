package me.lozm.app.board.controller;

import com.github.javafaker.Faker;
import me.lozm.app.board.service.BoardService;
import me.lozm.domain.board.vo.BoardDetailVo;
import me.lozm.global.code.CommentType;
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

import static me.lozm.app.board.BoardTestUtils.createBoard;
import static me.lozm.global.documentation.DocumentationUtils.PREFIX_DATA;
import static me.lozm.global.documentation.DocumentationUtils.PREFIX_PAGE_DATA;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends BaseDocumentationTest {

    @Autowired
    private BoardService boardService;


    @DisplayName("댓글 목록 조회(페이징) 성공")
    @Test
    void getComments_success() throws Exception {
        // Given
        BoardDetailVo.Response boardDetailVo = createBoard(boardService);

        // When
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/boards/{boardId}/comments", boardDetailVo.getBoardId())
                        .header(HttpHeaders.AUTHORIZATION, DocumentationUtils.getAccessToken())
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(this.documentationHandler.document(
                        DocumentationUtils.getPageParameters(),
                        responseFields(DocumentationUtils.getSuccessDefaultResponse())
                                .andWithPrefix(PREFIX_DATA, DocumentationUtils.getPageFieldDescriptor())
                                .andWithPrefix(PREFIX_PAGE_DATA, getCommentElementResponseDto())
                ));
    }

    @DisplayName("댓글 생성 성공")
    @Test
    void createComment_success() throws Exception {
        // Given
        BoardDetailVo.Response boardDetailVo = createBoard(boardService);

        final Faker faker = new Faker();

        final CommentType commentType = CommentType.NOTICE;
        final String content = faker.lorem().sentence(10);

        final String requestBody = "{\n" +
                "  \"commentType\": \"" + commentType + "\",\n" +
                "  \"content\": \"" + content + "\"\n" +
                "}";

        // When
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.post("/boards/{boardId}/comments", boardDetailVo.getBoardId())
                        .header(HttpHeaders.AUTHORIZATION, DocumentationUtils.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(this.documentationHandler.document(
                        requestFields(
                                fieldWithPath("commentType").type(JsonFieldType.STRING).description(DocumentationUtils.getAllOfEnumElementNames("댓글 유형", CommentType.class)),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("내용")
                        ),
                        responseFields(DocumentationUtils.getSuccessDefaultResponse())
                                .andWithPrefix(PREFIX_DATA, getCommentDetailResponseDto())
                ));
    }

    private List<FieldDescriptor> getCommentDetailResponseDto() {
        return List.of(
                subsectionWithPath("boardId").type(JsonFieldType.NUMBER).description("게시글 ID"),
                subsectionWithPath("commentId").type(JsonFieldType.NUMBER).description("댓글 ID"),
                subsectionWithPath("hierarchy").type(JsonFieldType.OBJECT).description("계층 게시글").optional(),
                subsectionWithPath("hierarchy.commonParentId").type(JsonFieldType.NUMBER).description("공통 상위 게시글 ID").optional(),
                subsectionWithPath("hierarchy.parentId").type(JsonFieldType.NUMBER).description("상위 게시글 ID").optional(),
                subsectionWithPath("hierarchy.groupOrder").type(JsonFieldType.NUMBER).description("그룹 순서").optional(),
                subsectionWithPath("hierarchy.groupLayer").type(JsonFieldType.NUMBER).description("그룹 레이어").optional(),
                subsectionWithPath("commentType").type(JsonFieldType.STRING).description(DocumentationUtils.getAllOfEnumElementNames("댓글 유형", CommentType.class)),
                subsectionWithPath("content").type(JsonFieldType.STRING).description("내용")
        );
    }

    private List<FieldDescriptor> getCommentElementResponseDto() {
        return List.of(
                subsectionWithPath("commentId").type(JsonFieldType.NUMBER).description("댓글 ID"),
                subsectionWithPath("hierarchy").type(JsonFieldType.OBJECT).description("계층 게시글").optional(),
                subsectionWithPath("hierarchy.commonParentId").type(JsonFieldType.NUMBER).description("공통 상위 게시글 ID").optional(),
                subsectionWithPath("hierarchy.parentId").type(JsonFieldType.NUMBER).description("상위 게시글 ID").optional(),
                subsectionWithPath("hierarchy.groupOrder").type(JsonFieldType.NUMBER).description("그룹 순서").optional(),
                subsectionWithPath("hierarchy.groupLayer").type(JsonFieldType.NUMBER).description("그룹 레이어").optional(),
                subsectionWithPath("commentType").type(JsonFieldType.STRING).description(DocumentationUtils.getAllOfEnumElementNames("댓글 유형", CommentType.class)),
                subsectionWithPath("content").type(JsonFieldType.STRING).description("내용")
        );
    }

}
