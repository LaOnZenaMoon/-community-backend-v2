package me.lozm.app.board.controller;

import com.github.javafaker.Faker;
import me.lozm.app.board.service.BoardService;
import me.lozm.domain.board.entity.Board;
import me.lozm.domain.board.service.BoardHelperService;
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

import static me.lozm.app.board.BoardTestUtils.createBoard;
import static me.lozm.global.documentation.DocumentationUtils.PREFIX_DATA;
import static me.lozm.global.documentation.DocumentationUtils.PREFIX_PAGE_DATA;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class BoardControllerTest extends BaseDocumentationTest {

    @Autowired
    private BoardService boardService;

    @Autowired
    private BoardHelperService boardHelperService;


    @DisplayName("게시글 목록 조회(페이징) 성공")
    @Test
    void getBoards_success() throws Exception {
        // Given
        for (int i = 0; i < 77; i++) {
            createBoard(boardService);
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
        BoardDetailVo.Response boardDetailVo = createBoard(boardService);
        final Long boardId = boardDetailVo.getBoardId();
        final Long viewCount = boardDetailVo.getViewCount();

        // When
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/boards/{boardId}", boardId)
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

        Board board = boardHelperService.getBoard(boardId);
        assertEquals(boardId, board.getId());
        assertEquals(viewCount + 1, board.getViewCount());
    }

    @DisplayName("게시글 생성 성공")
    @Test
    void createBoard_success() throws Exception {
        // Given
        final Faker faker = new Faker();

        final BoardType boardType = BoardType.MARKET;
        final ContentType contentType = ContentType.NOTICE;
        final String title = faker.book().title();
        final String content = faker.lorem().sentence(10);

        final String requestBody = "{\n" +
                "  \"boardType\": \"" + boardType + "\",\n" +
                "  \"contentType\": \"" + contentType + "\",\n" +
                "  \"title\": \"" + title + "\",\n" +
                "  \"content\": \"" + content + "\"\n" +
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

//        MockHttpServletResponse response = resultActions.andReturn().getResponse();
//        BoardDetailDto.Response responseDto = objectMapper.readValue(response.getContentAsString(), BoardDetailDto.Response.class);
//
//        assertEquals(boardType, responseDto.getBoardType());
//        assertEquals(contentType, responseDto.getContentType());
//        assertEquals(title, responseDto.getTitle());
//        assertEquals(content, responseDto.getContent());
    }

    @DisplayName("게시글 수정 성공")
    @Test
    void updateBoard_success() throws Exception {
        // Given
        BoardDetailVo.Response boardDetailVo = createBoard(boardService);

        final Long boardId = boardDetailVo.getBoardId();
        final BoardType boardType = BoardType.NEWS;
        final ContentType contentType = ContentType.EVENT;
        final String title = boardDetailVo.getTitle() + " updated";
        final String content = boardDetailVo.getContent() + " updated";

        final String requestBody = "{\n" +
                "  \"boardId\": " + boardId + ",\n" +
                "  \"boardType\": \"" + boardType + "\",\n" +
                "  \"contentType\": \"" + contentType + "\",\n" +
                "  \"title\": \"" + title + "\",\n" +
                "  \"content\": \"" + content + "\"\n" +
                "}";

        // When
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.put("/boards")
                        .header(HttpHeaders.AUTHORIZATION, DocumentationUtils.getAccessToken())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody)
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(this.documentationHandler.document(
                        requestFields(
                                fieldWithPath("boardId").type(JsonFieldType.NUMBER).description("게시글 ID"),
                                fieldWithPath("boardType").type(JsonFieldType.STRING).description(DocumentationUtils.getAllOfEnumElementNames("게시글 유형", BoardType.class)).optional(),
                                fieldWithPath("contentType").type(JsonFieldType.STRING).description(DocumentationUtils.getAllOfEnumElementNames("게시글 내용 유형", ContentType.class)).optional(),
                                fieldWithPath("title").type(JsonFieldType.STRING).description("제목").optional(),
                                fieldWithPath("content").type(JsonFieldType.STRING).description("내용").optional()
                        ),
                        responseFields(DocumentationUtils.getSuccessDefaultResponse())
                                .andWithPrefix(PREFIX_DATA, getBoardDetailResponseDto())
                ));

        Board board = boardHelperService.getBoard(boardId);
        assertEquals(boardId, board.getId());
        assertEquals(boardType, board.getBoardType());
        assertEquals(contentType, board.getContentType());
        assertEquals(title, board.getTitle());
        assertEquals(content, board.getContent());
    }

    @DisplayName("게시글 삭제 성공")
    @Test
    void deleteBoard_success() throws Exception {
        // Given
        BoardDetailVo.Response board = createBoard(boardService);
        final Long boardId = board.getBoardId();

        // When
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.delete("/boards/{boardId}", boardId)
                        .header(HttpHeaders.AUTHORIZATION, DocumentationUtils.getAccessToken())
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().is2xxSuccessful())
                .andDo(this.documentationHandler.document(
                        pathParameters(parameterWithName("boardId").description("게시글 ID")),
                        responseFields(DocumentationUtils.getSuccessDefaultResponse())
                ));

        assertThrows(IllegalArgumentException.class, () -> boardService.getBoardDetail(boardId));
    }

    private List<FieldDescriptor> getBoardDetailResponseDto() {
        return List.of(
                subsectionWithPath("boardId").type(JsonFieldType.NUMBER).description("게시글 ID"),
                subsectionWithPath("hierarchy").type(JsonFieldType.OBJECT).description("계층 게시글").optional(),
                subsectionWithPath("hierarchy.commonParentId").type(JsonFieldType.NUMBER).description("공통 상위 게시글 ID").optional(),
                subsectionWithPath("hierarchy.parentId").type(JsonFieldType.NUMBER).description("상위 게시글 ID").optional(),
                subsectionWithPath("hierarchy.groupOrder").type(JsonFieldType.NUMBER).description("그룹 순서").optional(),
                subsectionWithPath("hierarchy.groupLayer").type(JsonFieldType.NUMBER).description("그룹 레이어").optional(),
                subsectionWithPath("boardType").type(JsonFieldType.STRING).description(DocumentationUtils.getAllOfEnumElementNames("게시글 유형", BoardType.class)),
                subsectionWithPath("contentType").type(JsonFieldType.STRING).description(DocumentationUtils.getAllOfEnumElementNames("게시글 내용 유형", ContentType.class)),
                subsectionWithPath("viewCount").type(JsonFieldType.NUMBER).description("게시글 조회수"),
                subsectionWithPath("title").type(JsonFieldType.STRING).description("제목"),
                subsectionWithPath("content").type(JsonFieldType.STRING).description("내용")
        );
    }

}
