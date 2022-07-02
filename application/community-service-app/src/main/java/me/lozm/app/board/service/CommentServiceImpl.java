package me.lozm.app.board.service;

import lombok.RequiredArgsConstructor;
import me.lozm.domain.board.entity.Board;
import me.lozm.domain.board.entity.Comment;
import me.lozm.domain.board.mapper.CommentMapper;
import me.lozm.domain.board.repository.CommentRepository;
import me.lozm.domain.board.service.BoardHelperService;
import me.lozm.domain.board.service.CommentHelperService;
import me.lozm.domain.board.vo.CommentCreateVo;
import me.lozm.domain.board.vo.CommentDetailVo;
import me.lozm.domain.board.vo.CommentPageVo;
import me.lozm.domain.board.vo.CommentUpdateVo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final CommentHelperService commentHelperService;
    private final CommentMapper commentMapper;
    private final BoardHelperService boardHelperService;


    @Override
    public Page<CommentPageVo.Element> getComments(CommentPageVo.Request requestVo) {
        return commentRepository.findComments(requestVo);
    }

    @Override
    @Transactional
    public CommentDetailVo.Response createComment(CommentCreateVo.Request commentCreateVo) {
        Board board = boardHelperService.getBoard(commentCreateVo.getBoardId());
        Comment comment = commentRepository.save(Comment.of(board, commentCreateVo));
        return commentMapper.toDetailVo(comment);
    }

    @Override
    @Transactional
    public CommentDetailVo.Response updateComment(CommentUpdateVo.Request commentUpdateVo) {
        boardHelperService.getBoard(commentUpdateVo.getBoardId());
        Comment comment = commentHelperService.getComment(commentUpdateVo.getCommentId());
        comment.update(commentUpdateVo);
        return commentMapper.toDetailVo(comment);
    }

    @Override
    @Transactional
    public void deleteComment(Long boardId, Long commentId) {
        boardHelperService.getBoard(boardId);
        Comment comment = commentHelperService.getComment(commentId);
        comment.updateIsUse(false);
    }

}
