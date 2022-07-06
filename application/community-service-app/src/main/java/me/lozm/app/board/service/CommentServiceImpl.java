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
import me.lozm.global.code.HierarchyType;
import me.lozm.utils.exception.BadRequestException;
import me.lozm.utils.exception.CustomExceptionType;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static java.lang.String.format;

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

        final HierarchyType hierarchyType = commentCreateVo.getHierarchy().getHierarchyType();
        if (hierarchyType == HierarchyType.ORIGIN) {
            comment.getHierarchy().update(comment.getId());
            return commentMapper.toDetailVo(comment);
        }

        final Long parentCommentId = commentCreateVo.getHierarchy().getParentId();
        Comment parentComment = commentHelperService.getComment(parentCommentId);

        if (hierarchyType == HierarchyType.REPLY_FOR_ORIGIN) {
            Integer maxGroupOrder = getMaxGroupOrderWhenReplyForOrigin(parentCommentId);

            comment.getHierarchy().update(
                    parentCommentId,
                    parentCommentId,
                    parentComment.getHierarchy().getGroupLayer() + 1,
                    maxGroupOrder + 1
            );

        } else if (hierarchyType == HierarchyType.REPLY_FOR_REPLY) {
            Integer maxGroupOrder = getMaxGroupOrderWhenReplyForReply(parentComment);

            comment.getHierarchy().update(
                    parentComment.getHierarchy().getCommonParentId(),
                    parentCommentId,
                    parentComment.getHierarchy().getGroupLayer() + 1,
                    maxGroupOrder + 1
            );

            increaseCommentsGroupOrderBehindCreatedComment(parentComment, maxGroupOrder);

        } else {
            throw new BadRequestException(CustomExceptionType.INVALID_HIERARCHY_TYPE);
        }

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

    private Integer getMaxGroupOrderWhenReplyForOrigin(Long parentCommentId) {
        return commentRepository.findMaxGroupOrder(parentCommentId)
                .orElseThrow(() -> new IllegalStateException(format("groupOrder 처리에 오류가 발생하였습니다. parentCommentId: %d", parentCommentId)));
    }

    private void increaseCommentsGroupOrderBehindCreatedComment(Comment parentComment, Integer maxGroupOrder) {
        List<Comment> commentList = commentRepository.findAllByHierarchy_CommonParentId(parentComment.getHierarchy().getCommonParentId());
        for (Comment comment1 : commentList) {
            if (comment1.getHierarchy().getGroupOrder() > maxGroupOrder + 1) {
                comment1.getHierarchy().increaseGroupOrder();
            }
        }
    }

    private Integer getMaxGroupOrderWhenReplyForReply(Comment parentComment) {
        final Long commonParentId = parentComment.getHierarchy().getCommonParentId();
        final Long parentId = parentComment.getHierarchy().getParentId();

        Optional<Integer> maxGroupOrderOptional1 = commentRepository.findMaxGroupOrder(commonParentId, parentComment.getId());
        if (maxGroupOrderOptional1.isPresent()) {
            return maxGroupOrderOptional1.get();
        }

        Optional<Integer> maxGroupOrderOptional2 = commentRepository.findMaxGroupOrder(commonParentId, parentId);
        if (maxGroupOrderOptional2.isPresent()) {
            return maxGroupOrderOptional2.get();
        }

        throw new IllegalStateException(format("댓글 groupOrder 처리에 오류가 발생하였습니다. commonParentId: %d, parentId: %d"
                , commonParentId, parentId));
    }

}
