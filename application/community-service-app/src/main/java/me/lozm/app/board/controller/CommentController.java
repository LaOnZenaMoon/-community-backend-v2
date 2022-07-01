package me.lozm.app.board.controller;

import lombok.RequiredArgsConstructor;
import me.lozm.app.board.service.CommentService;
import me.lozm.domain.board.dto.CommentPageDto;
import me.lozm.domain.board.mapper.CommentMapper;
import me.lozm.domain.board.vo.CommentPageVo;
import me.lozm.global.model.CommonResponseDto;
import me.lozm.global.model.dto.CommonPageResponseDto;
import me.lozm.global.model.dto.PageQueryParameters;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class CommentController {

    private final CommentService commentService;
    private final CommentMapper commentMapper;


    @GetMapping("boards/{boardId}/comments")
    public CommonResponseDto<CommonPageResponseDto<CommentPageDto.Response>> getComments(PageQueryParameters pageQueryParameters,
                                                                                         @PathVariable("boardId") Long boardId) {

        CommentPageVo.Request requestVo = commentMapper.toPageVo(boardId, pageQueryParameters);
        Page<CommentPageVo.Element> responsePageVo = commentService.getComments(requestVo);

        List<CommentPageDto.Response> responseDtoList = responsePageVo.getContent().stream().map(commentMapper::toPageDto).collect(Collectors.toList());
        return CommonResponseDto.success(new CommonPageResponseDto<>(responsePageVo, responseDtoList));
    }

}
