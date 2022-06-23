package me.lozm.domain.board.mapper;

import me.lozm.domain.board.dto.BoardCreateDto;
import me.lozm.domain.board.dto.BoardPageDto;
import me.lozm.domain.board.entity.Board;
import me.lozm.domain.board.vo.BoardCreateVo;
import me.lozm.domain.board.vo.BoardPageVo;
import me.lozm.global.model.dto.PageQueryParameters;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BoardMapper {

    @Mapping(source = "pageQueryParameters", target = "pageQueryParameters")
    BoardPageVo.Request toPageVo(PageQueryParameters pageQueryParameters);
    
    BoardPageDto.Response toPageDto(BoardPageVo.Element element);

    BoardCreateVo.Request toCreateVo(BoardCreateDto.Request requestDto);

    BoardCreateVo.Response toCreateVo(Board board);
}
