package me.lozm.domain.board.service;

import lombok.RequiredArgsConstructor;
import me.lozm.domain.board.entity.Comment;
import me.lozm.domain.board.repository.CommentRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class CommentHelperService {

    private final CommentRepository commentRepository;

    public Optional<Comment> findComment(Long commentId) {
        return commentRepository.findById(commentId);
    }

    public Comment getComment(Long commentId) {
        return findComment(commentId).orElseThrow(() -> new IllegalArgumentException(getNotFoundFormat(commentId)));
    }

    public Optional<Comment> findCommentUsingLock(Long commentId) {
        return commentRepository.findByIdUsingLock(commentId);
    }

    public Comment getCommentUsingLock(Long commentId) {
        return findCommentUsingLock(commentId).orElseThrow(() -> new IllegalArgumentException(getNotFoundFormat(commentId)));
    }

    private String getNotFoundFormat(Long commentId) {
        return format("존재하지 않는 댓글입니다. 댓글 ID: %d", commentId);
    }

}
