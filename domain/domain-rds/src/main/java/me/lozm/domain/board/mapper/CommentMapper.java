package me.lozm.domain.board.mapper;

import me.lozm.domain.board.dto.CommentPageDto;
import me.lozm.domain.board.vo.CommentPageVo;
import me.lozm.global.model.dto.PageQueryParameters;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CommentMapper {
    @Mapping(source = "boardId", target = "boardId")
    @Mapping(source = "pageQueryParameters", target = "pageQueryParameters")
    CommentPageVo.Request toPageVo(Long boardId, PageQueryParameters pageQueryParameters);

    CommentPageDto.Response toPageDto(CommentPageVo.Element element);
}
