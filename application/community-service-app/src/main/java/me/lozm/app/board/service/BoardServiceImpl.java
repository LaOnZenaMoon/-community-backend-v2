package me.lozm.app.board.service;

import lombok.RequiredArgsConstructor;
import me.lozm.domain.board.entity.Board;
import me.lozm.domain.board.entity.Comment;
import me.lozm.domain.board.mapper.BoardMapper;
import me.lozm.domain.board.repository.BoardRepository;
import me.lozm.domain.board.service.BoardHelperService;
import me.lozm.domain.board.vo.BoardCreateVo;
import me.lozm.domain.board.vo.BoardDetailVo;
import me.lozm.domain.board.vo.BoardPageVo;
import me.lozm.domain.board.vo.BoardUpdateVo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardHelperService boardHelperService;
    private final BoardMapper boardMapper;


    @Override
    public Page<BoardPageVo.Element> getBoards(BoardPageVo.Request requestVo) {
        return boardRepository.findBoards(requestVo);
    }

    @Override
    @Transactional
    public BoardDetailVo.Response getBoardDetail(Long boardId) {
        Board board = boardHelperService.getBoardUsingLock(boardId);
        board.addViewCount();
        return boardMapper.toDetailVo(board);
    }

    @Override
    @Transactional
    public BoardDetailVo.Response createBoard(BoardCreateVo.Request requestVo) {
        Board board = boardRepository.save(Board.from(requestVo));
        return boardMapper.toDetailVo(board);
    }
    @Override
    @Transactional
    public BoardDetailVo.Response updateBoard(BoardUpdateVo.Request boardUpdateVo) {
        Board board = boardHelperService.getBoardUsingLock(boardUpdateVo.getBoardId());
        board.update(boardUpdateVo);
        return boardMapper.toDetailVo(board);
    }

    @Override
    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = boardHelperService.getBoardUsingLock(boardId);
        board.updateIsUse(false);
        for (Comment comment : board.getComments()) {
            comment.updateIsUse(false);
        }
    }

}
