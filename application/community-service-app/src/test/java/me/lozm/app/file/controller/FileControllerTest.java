package me.lozm.app.file.controller;

import me.lozm.app.board.service.BoardService;
import me.lozm.app.file.service.FileService;
import me.lozm.domain.board.code.BoardType;
import me.lozm.domain.board.code.ContentType;
import me.lozm.domain.board.vo.BoardDetailVo;
import me.lozm.domain.file.code.FileUploadType;
import me.lozm.domain.file.repository.FileRepository;
import me.lozm.domain.file.vo.FileUploadVo;
import me.lozm.global.documentation.BaseDocumentationTest;
import me.lozm.global.documentation.DocumentationUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Optional;

import static me.lozm.app.board.TestUtils.createBoard;
import static me.lozm.global.documentation.DocumentationUtils.PREFIX_DATA;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.restdocs.payload.PayloadDocumentation.responseFields;
import static org.springframework.restdocs.payload.PayloadDocumentation.subsectionWithPath;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class FileControllerTest extends BaseDocumentationTest {

    @Autowired
    private FileService fileService;

    @Autowired
    private FileRepository fileRepository;


    private static BoardDetailVo.Response boardDetailVo;

    @BeforeAll
    static void beforeAll(@Autowired BoardService boardService) {
        boardDetailVo = createBoard(BoardType.NEWS, ContentType.GENERAL, boardService);
    }

    @AfterAll
    static void afterAll(@Autowired BoardService boardService) {
        boardService.deleteBoard(boardDetailVo.getBoardId());
    }


    @DisplayName("파일 업로드 성공")
    @Test
    void uploadFile_success() throws Exception {
        // Given
        String requestBody = "{" +
                "\"uploadType\": \"" + FileUploadType.BOARD.getCode() + "\", " +
                "\"targetId\": " + boardDetailVo.getBoardId() +
                "}";

        MockMultipartFile requestDto = new MockMultipartFile(
                "requestDto", "", MediaType.APPLICATION_JSON_VALUE, requestBody.getBytes());

        MockMultipartFile uploadMultipartFile = getSampleMockMultipartFile();

        // When
        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.fileUpload("/files")
                        .file(requestDto)
                        .file(uploadMultipartFile)
        );

        // Then
        resultActions.andDo(print())
                .andExpect(status().isCreated())
                .andDo(this.documentationHandler.document(
                        responseFields(DocumentationUtils.getSuccessDefaultResponse())
                                .andWithPrefix(
                                        PREFIX_DATA,
                                        subsectionWithPath("fileId").type(JsonFieldType.STRING).description("파일 ID"),
                                        subsectionWithPath("fileName").type(JsonFieldType.STRING).description("파일명"),
                                        subsectionWithPath("uploadType").type(JsonFieldType.STRING).description(DocumentationUtils.getAllOfEnumElementNames("파일 업로드 유형", FileUploadType.class)),
                                        subsectionWithPath("targetId").type(JsonFieldType.NUMBER).description("업로드할 대상 ID")
                                )
                ));

        Optional<me.lozm.domain.file.entity.File> fileOptional = fileRepository.findByTargetId(boardDetailVo.getBoardId());
        assertTrue(fileOptional.isPresent());
    }

    @DisplayName("파일 다운로드 성공")
    @Test
    void downloadFile_success() throws Exception {
        // Given
        MockMultipartFile sampleMockMultipartFile = getSampleMockMultipartFile();

        // When
        FileUploadVo.Response fileUploadResponseVo = fileService.uploadFile(
                new FileUploadVo.Request(sampleMockMultipartFile, FileUploadType.BOARD, boardDetailVo.getBoardId()));

        ResultActions resultActions = mockMvc.perform(
                RestDocumentationRequestBuilders.get("/files/{fileId}", fileUploadResponseVo.getFileId())
        );

        // Then
        resultActions.andDo(print())
                .andDo(this.documentationHandler.document(
                        pathParameters(parameterWithName("fileId").description("파일 ID"))
                ));
    }

    private MockMultipartFile getSampleMockMultipartFile() throws IOException {
        ClassPathResource classPathResource = new ClassPathResource("files/sample.jpg");
        File uploadFile = classPathResource.getFile();
        FileInputStream fileInputStream = new FileInputStream(uploadFile);
        MockMultipartFile uploadMultipartFile = new MockMultipartFile(
                "uploadFile", uploadFile.getName(), MediaType.IMAGE_JPEG_VALUE, fileInputStream);
        return uploadMultipartFile;
    }

}