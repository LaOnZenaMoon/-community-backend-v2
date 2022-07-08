package me.lozm.domain.board.repository;

import me.lozm.domain.board.entity.Comment;
import me.lozm.domain.board.entity.QComment;
import me.lozm.domain.board.vo.CommentPageVo;
import me.lozm.domain.board.vo.QCommentPageVo_Element;
import me.lozm.global.querydsl.Querydsl4RepositorySupport;
import org.springframework.data.domain.Page;

public class CommentRepositoryImpl extends Querydsl4RepositorySupport<Comment> implements CommentRepositoryCustom {

    public CommentRepositoryImpl() {
        super(Comment.class);
    }


    @Override
    public Page<CommentPageVo.Element> findComments(CommentPageVo.Request requestVo) {
        return applyPagination(requestVo.getPageQueryParameters().getPageRequest(), query ->
                select(new QCommentPageVo_Element(
                        QComment.comment.id,
                        QComment.comment.hierarchy,
                        QComment.comment.commentType,
                        QComment.comment.content
                ))
                        .from(QComment.comment)
                        .where(QComment.comment.board.id.eq(requestVo.getBoardId()))
                        .orderBy(QComment.comment.hierarchy.commonParentId.desc(), QComment.comment.hierarchy.groupOrder.asc())
        );
    }

}
