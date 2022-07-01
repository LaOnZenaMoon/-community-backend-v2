package me.lozm.app.board.controller;

import me.lozm.app.board.service.BoardService;
import me.lozm.domain.board.vo.BoardDetailVo;
import me.lozm.global.code.CommentType;
import me.lozm.global.documentation.BaseDocumentationTest;
import me.lozm.global.documentation.DocumentationUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.FieldDescriptor;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static me.lozm.app.board.BoardTestUtils.createBoard;
import static me.lozm.global.documentation.DocumentationUtils.PREFIX_DATA;
import static me.lozm.global.documentation.DocumentationUtils.PREFIX_PAGE_DATA;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CommentControllerTest extends BaseDocumentationTest {

    @Autowired
    private BoardService boardService;


    @DisplayName("댓글 목록 조회(페이징) 성공")
    @Test
    void getBoards_success() throws Exception {
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
                                .andWithPrefix(PREFIX_PAGE_DATA, getCommentDetailResponseDto())
                ));
    }

    private List<FieldDescriptor> getCommentDetailResponseDto() {
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
