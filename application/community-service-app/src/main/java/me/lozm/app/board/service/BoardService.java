package me.lozm.app.board.service;

import me.lozm.domain.board.vo.BoardCreateVo;
import me.lozm.domain.board.vo.BoardDetailVo;
import me.lozm.domain.board.vo.BoardPageVo;
import me.lozm.domain.board.vo.BoardUpdateVo;
import org.springframework.data.domain.Page;

public interface BoardService {

    Page<BoardPageVo.Element> getBoards(BoardPageVo.Request requestVo);

    BoardDetailVo.Response getBoardDetail(Long boardId);

    BoardDetailVo.Response createBoard(BoardCreateVo.Request requestVo);

    BoardDetailVo.Response updateBoard(BoardUpdateVo.Request boardUpdateVo);

    void deleteBoard(Long boardId);
}
