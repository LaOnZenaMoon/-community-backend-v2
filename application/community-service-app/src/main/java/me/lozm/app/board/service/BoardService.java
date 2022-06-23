package me.lozm.app.board.service;

import lombok.RequiredArgsConstructor;
import me.lozm.domain.board.entity.Board;
import me.lozm.domain.board.mapper.BoardMapper;
import me.lozm.domain.board.repository.BoardRepository;
import me.lozm.domain.board.vo.BoardCreateVo;
import me.lozm.domain.board.vo.BoardPageVo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;
    private final BoardMapper boardMapper;


    public Page<BoardPageVo.Element> getBoards(BoardPageVo.Request requestVo) {
        return boardRepository.findBoards(requestVo);
    }

    @Transactional
    public BoardCreateVo.Response createBoard(BoardCreateVo.Request requestVo) {
        Board board = boardRepository.save(Board.from(requestVo));
        return boardMapper.toCreateVo(board);
    }

}
