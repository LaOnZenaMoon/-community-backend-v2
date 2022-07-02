package me.lozm.domain.board.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.domain.board.vo.CommentCreateVo;
import me.lozm.domain.board.vo.CommentUpdateVo;
import me.lozm.global.code.CommentType;
import me.lozm.global.code.converter.CommentTypeConverter;
import me.lozm.global.model.entity.BaseEntity;
import me.lozm.global.model.entity.HierarchicalEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;


@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@Table(name = "COMMENTS")
@Where(clause = "IS_USE = true")
@DynamicUpdate
@DynamicInsert
@SequenceGenerator(name = "COMMENT_SEQ_GEN", sequenceName = "COMMENT_SEQ")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMMENT_SEQ_GEN")
    @Column(name = "COMMENT_ID")
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "commonParentId", column = @Column(name = "COMMON_PARENT_COMMENT_ID")),
            @AttributeOverride(name = "parentId", column = @Column(name = "PARENT_COMMENT_ID"))
    })
    private HierarchicalEntity hierarchicalComment;

    @Column(name = "COMMENT_TYPE")
    @Convert(converter = CommentTypeConverter.class)
    private CommentType commentType;

    @Lob
    @Column(name = "CONTENT")
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    public static Comment of(Board board, CommentCreateVo.Request commentCreateVo) {
        Comment comment = new Comment();
        comment.isUse = true;
        comment.board = board;
        comment.commentType = commentCreateVo.getCommentType();
        comment.content = commentCreateVo.getContent();
        return comment;
    }

    public void update(CommentUpdateVo.Request commentUpdateVo) {
        if (isNotEmpty(commentUpdateVo.getCommentType())) {
            this.commentType = commentUpdateVo.getCommentType();
        }

        if (isNotBlank(commentUpdateVo.getContent())) {
            this.content = commentUpdateVo.getContent();
        }
    }

}
