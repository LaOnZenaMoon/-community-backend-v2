package me.lozm.app.board.controller;

import lombok.RequiredArgsConstructor;
import me.lozm.app.board.service.BoardService;
import me.lozm.domain.board.dto.BoardPageDto;
import me.lozm.domain.board.mapper.BoardMapper;
import me.lozm.domain.board.vo.BoardPageVo;
import me.lozm.global.model.dto.CommonPageResponseDto;
import me.lozm.global.model.CommonResponseDto;
import me.lozm.global.model.dto.PageQueryParameters;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("boards")
@RestController
@RequiredArgsConstructor
public class BoardController {

    private final BoardService boardService;
    private final BoardMapper boardMapper;


    @GetMapping
    public CommonResponseDto<CommonPageResponseDto<BoardPageDto.Response>> getBoards(PageQueryParameters pageQueryParameters) {
        BoardPageVo.Request requestVo = boardMapper.toPageVo(pageQueryParameters);
        Page<BoardPageVo.Element> responsePageVo = boardService.getBoards(requestVo);

        List<BoardPageDto.Response> responseDtoList = responsePageVo.getContent().stream().map(boardMapper::toPageDto).collect(Collectors.toList());
        return CommonResponseDto.success(new CommonPageResponseDto<>(responsePageVo, responseDtoList));
    }

}
