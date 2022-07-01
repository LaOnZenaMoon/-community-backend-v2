package me.lozm.app.board.service;

import me.lozm.domain.board.vo.CommentPageVo;
import org.springframework.data.domain.Page;

public interface CommentService {
    Page<CommentPageVo.Element> getComments(CommentPageVo.Request requestVo);
}
