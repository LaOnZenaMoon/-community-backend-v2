package me.lozm.app.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.RequiredArgsConstructor;
import me.lozm.app.board.service.BoardService;
import me.lozm.app.user.service.AuthService;
import me.lozm.app.user.service.UserService;
import me.lozm.domain.board.dto.BoardPageDto;
import me.lozm.domain.board.mapper.BoardMapper;
import me.lozm.domain.board.vo.BoardPageVo;
import me.lozm.global.model.CommonResponseDto;
import me.lozm.global.model.dto.CommonPageResponseDto;
import me.lozm.global.model.dto.PageQueryParameters;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RequestMapping("users")
@RestController
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AuthService authService;


    @GetMapping
    public CommonResponseDto<CommonPageResponseDto<BoardPageDto.Response>> getBoards(PageQueryParameters pageQueryParameters) throws JsonProcessingException {
        authService.createToken();
        return CommonResponseDto.success();
    }

}
