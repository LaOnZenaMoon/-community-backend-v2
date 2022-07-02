package me.lozm.app.board.service;

import me.lozm.domain.board.vo.CommentCreateVo;
import me.lozm.domain.board.vo.CommentDetailVo;
import me.lozm.domain.board.vo.CommentPageVo;
import me.lozm.domain.board.vo.CommentUpdateVo;
import org.springframework.data.domain.Page;

public interface CommentService {
    Page<CommentPageVo.Element> getComments(CommentPageVo.Request requestVo);

    CommentDetailVo.Response createComment(CommentCreateVo.Request commentCreateVo);

    CommentDetailVo.Response updateComment(CommentUpdateVo.Request commentUpdateVo);

    void deleteComment(Long boardId, Long commentId);
}
