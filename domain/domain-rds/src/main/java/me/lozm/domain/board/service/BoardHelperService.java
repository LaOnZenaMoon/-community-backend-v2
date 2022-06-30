package me.lozm.domain.board.service;

import lombok.RequiredArgsConstructor;
import me.lozm.domain.board.entity.Board;
import me.lozm.domain.board.repository.BoardRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class BoardHelperService {

    private final BoardRepository boardRepository;

    public Optional<Board> findBoard(Long boardId) {
        return boardRepository.findById(boardId);
    }

    public Board getBoard(Long boardId) {
        return findBoard(boardId).orElseThrow(() -> new IllegalArgumentException(getNotFoundFormat(boardId)));
    }

    public Optional<Board> findBoardUsingLock(Long boardId) {
        return boardRepository.findByIdUsingLock(boardId);
    }

    public Board getBoardUsingLock(Long boardId) {
        return findBoardUsingLock(boardId).orElseThrow(() -> new IllegalArgumentException(getNotFoundFormat(boardId)));
    }

    private String getNotFoundFormat(Long boardId) {
        return format("존재하지 않는 게시글입니다. 게시글 ID: %d", boardId);
    }

}
