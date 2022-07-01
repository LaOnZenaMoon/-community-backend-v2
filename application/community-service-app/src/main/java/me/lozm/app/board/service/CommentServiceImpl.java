package me.lozm.app.board.service;

import lombok.RequiredArgsConstructor;
import me.lozm.domain.board.repository.CommentRepository;
import me.lozm.domain.board.vo.CommentPageVo;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;


    @Override
    public Page<CommentPageVo.Element> getComments(CommentPageVo.Request requestVo) {
        return commentRepository.findComments(requestVo);
    }

}
