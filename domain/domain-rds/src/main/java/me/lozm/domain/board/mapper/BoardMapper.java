package me.lozm.domain.board.mapper;

import me.lozm.domain.board.dto.BoardCreateDto;
import me.lozm.domain.board.dto.BoardDetailDto;
import me.lozm.domain.board.dto.BoardPageDto;
import me.lozm.domain.board.dto.BoardUpdateDto;
import me.lozm.domain.board.entity.Board;
import me.lozm.domain.board.vo.BoardCreateVo;
import me.lozm.domain.board.vo.BoardDetailVo;
import me.lozm.domain.board.vo.BoardPageVo;
import me.lozm.domain.board.vo.BoardUpdateVo;
import me.lozm.global.model.dto.PageQueryParameters;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface BoardMapper {

    @Mapping(source = "pageQueryParameters", target = "pageQueryParameters")
    BoardPageVo.Request toPageVo(PageQueryParameters pageQueryParameters);
    
    BoardPageDto.Response toPageDto(BoardPageVo.Element element);

    BoardCreateVo.Request toCreateVo(BoardCreateDto.Request requestDto);

    @Mapping(source = "id", target = "boardId")
    BoardDetailVo.Response toDetailVo(Board board);

    BoardDetailDto.Response toDetailDto(BoardDetailVo.Response boardDetailVo);

    @Mapping(source = "requestDto.boardType", target = "boardType")
    @Mapping(source = "requestDto.contentType", target = "contentType")
    @Mapping(source = "requestDto.title", target = "title")
    @Mapping(source = "requestDto.content", target = "content")
    BoardUpdateVo.Request toUpdateVo(Long boardId, BoardUpdateDto.Request requestDto);
}
