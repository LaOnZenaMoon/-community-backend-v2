package me.lozm.app.board.service;

import lombok.RequiredArgsConstructor;
import me.lozm.domain.board.repository.BoardRepository;
import me.lozm.domain.board.vo.BoardPageVo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardService {

    private final BoardRepository boardRepository;


    public Page<BoardPageVo.Element> getBoards(BoardPageVo.Request requestVo) {
        return boardRepository.findBoards(requestVo);
    }

}
