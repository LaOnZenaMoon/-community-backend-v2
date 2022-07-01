package me.lozm.domain.board.repository;

import me.lozm.domain.board.vo.CommentPageVo;
import org.springframework.data.domain.Page;

public interface CommentRepositoryCustom {
    Page<CommentPageVo.Element> findComments(CommentPageVo.Request requestVo);
}
