package me.lozm.domain.board.repository;

import me.lozm.domain.board.vo.BoardPageVo;
import org.springframework.data.domain.Page;

public interface BoardRepositoryCustom {
    Page<BoardPageVo.Element> findBoards(BoardPageVo.Request requestVo);
}
