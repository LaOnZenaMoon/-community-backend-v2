package me.lozm.app.board.controller;

import lombok.RequiredArgsConstructor;
import me.lozm.app.board.service.BoardService;
import me.lozm.domain.board.dto.BoardCreateDto;
import me.lozm.domain.board.dto.BoardDetailDto;
import me.lozm.domain.board.dto.BoardPageDto;
import me.lozm.domain.board.dto.BoardUpdateDto;
import me.lozm.domain.board.mapper.BoardMapper;
import me.lozm.domain.board.vo.BoardCreateVo;
import me.lozm.domain.board.vo.BoardDetailVo;
import me.lozm.domain.board.vo.BoardPageVo;
import me.lozm.domain.board.vo.BoardUpdateVo;
import me.lozm.global.model.CommonResponseDto;
import me.lozm.global.model.dto.CommonPageResponseDto;
import me.lozm.global.model.dto.PageQueryParameters;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("boards")
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardMapper boardMapper;


    @GetMapping
    public ResponseEntity<CommonResponseDto<CommonPageResponseDto<BoardPageDto.Response>>> getBoards(PageQueryParameters pageQueryParameters) {
        BoardPageVo.Request requestVo = boardMapper.toPageVo(pageQueryParameters);
        Page<BoardPageVo.Element> responsePageVo = boardService.getBoards(requestVo);

        List<BoardPageDto.Response> responseDtoList = responsePageVo.getContent().stream().map(boardMapper::toPageDto).collect(Collectors.toList());
        return CommonResponseDto.ok(new CommonPageResponseDto<>(responsePageVo, responseDtoList));
    }

    @GetMapping("{boardId}")
    public ResponseEntity<CommonResponseDto<BoardDetailDto.Response>> getBoardDetail(@PathVariable("boardId") Long boardId) {
        BoardDetailVo.Response boardDetailVo = boardService.getBoardDetail(boardId);
        BoardDetailDto.Response responseDto = boardMapper.toDetailDto(boardDetailVo);
        return CommonResponseDto.ok(responseDto);
    }

    @PostMapping
    public ResponseEntity<CommonResponseDto<BoardDetailDto.Response>> createBoard(@RequestBody @Validated BoardCreateDto.Request requestDto) {
        BoardCreateVo.Request boardCreateVo = boardMapper.toCreateVo(requestDto);
        BoardDetailVo.Response boardDetailVo = boardService.createBoard(boardCreateVo);
        BoardDetailDto.Response responseDto = boardMapper.toDetailDto(boardDetailVo);
        return CommonResponseDto.created(responseDto);
    }

    @PutMapping("{boardId}")
    public ResponseEntity<CommonResponseDto<BoardDetailDto.Response>> updateBoard(@RequestBody @Validated BoardUpdateDto.Request requestDto,
                                                                                  @PathVariable("boardId") Long boardId) {
        BoardUpdateVo.Request boardUpdateVo = boardMapper.toUpdateVo(boardId, requestDto);
        BoardDetailVo.Response boardDetailVo = boardService.updateBoard(boardUpdateVo);
        BoardDetailDto.Response responseDto = boardMapper.toDetailDto(boardDetailVo);
        return CommonResponseDto.ok(responseDto);
    }

    @DeleteMapping("{boardId}")
    public ResponseEntity<CommonResponseDto<Object>> deleteBoard(@PathVariable("boardId") Long boardId) {
        boardService.deleteBoard(boardId);
        return CommonResponseDto.ok();
    }

}
