package me.lozm.domain.board.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.domain.board.vo.BoardCreateVo;
import me.lozm.domain.board.vo.BoardUpdateVo;
import me.lozm.global.code.BoardType;
import me.lozm.global.code.ContentType;
import me.lozm.global.code.converter.BoardTypeConverter;
import me.lozm.global.code.converter.ContentTypeConverter;
import me.lozm.global.model.entity.BaseEntity;
import me.lozm.global.model.entity.HierarchicalEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.util.List;

import static org.apache.commons.lang3.ObjectUtils.isNotEmpty;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@Table(name = "BOARD")
@Where(clause = "IS_USE = true")
@DynamicUpdate
@DynamicInsert
@SequenceGenerator(name = "BOARD_SEQ_GEN", sequenceName = "BOARD_SEQ")
public class Board extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "BOARD_SEQ_GEN")
    @Column(name = "BOARD_ID")
    private Long id;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "commonParentId", column = @Column(name = "COMMON_PARENT_BOARD_ID")),
            @AttributeOverride(name = "parentId", column = @Column(name = "PARENT_BOARD_ID"))
    })
    private HierarchicalEntity hierarchicalBoard;

    @Column(name = "BOARD_TYPE")
    @Convert(converter = BoardTypeConverter.class)
    private BoardType boardType;

    @Column(name = "CONTENT_TYPE")
    @Convert(converter = ContentTypeConverter.class)
    private ContentType contentType;

    @Column(name = "VIEW_COUNT")
    private Long viewCount;

    @Column(name = "TITLE")
    private String title;

    @Lob
    @Column(name = "CONTENT")
    private String content;

    @OneToMany(mappedBy = "board", cascade = CascadeType.ALL)
    private List<Comment> comments;


    public static Board from(BoardCreateVo.Request boardCreateVo) {
        Board board = new Board();
        board.isUse = true;
        board.boardType = boardCreateVo.getBoardType();
        board.contentType = boardCreateVo.getContentType();
        board.viewCount = 0L;
        board.title = boardCreateVo.getTitle();
        board.content = boardCreateVo.getContent();
        return board;
    }

    public void update(BoardUpdateVo.Request boardUpdateVo) {
        if (isNotEmpty(boardUpdateVo.getBoardType())) {
            this.boardType = boardUpdateVo.getBoardType();
        }

        if (isNotEmpty(boardUpdateVo.getContentType())) {
            this.contentType = boardUpdateVo.getContentType();
        }

        if (isNotBlank(boardUpdateVo.getTitle())) {
            this.title = boardUpdateVo.getTitle();
        }

        if (isNotBlank(boardUpdateVo.getContent())) {
            this.content = boardUpdateVo.getContent();
        }
    }

    public void updateIsUse(boolean isUse) {
        this.isUse = isUse;
    }

}
