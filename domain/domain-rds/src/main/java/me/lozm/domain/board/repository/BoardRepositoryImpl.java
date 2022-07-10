package me.lozm.domain.board.repository;

import me.lozm.domain.board.entity.Board;
import me.lozm.domain.board.entity.QBoard;
import me.lozm.domain.board.vo.BoardPageVo;
import me.lozm.domain.board.vo.QBoardPageVo_Element;
import me.lozm.global.querydsl.Querydsl4RepositorySupport;
import org.springframework.data.domain.Page;

public class BoardRepositoryImpl extends Querydsl4RepositorySupport<Board> implements BoardRepositoryCustom {

    public BoardRepositoryImpl() {
        super(Board.class);
    }


    @Override
    public Page<BoardPageVo.Element> findBoards(BoardPageVo.Request requestVo) {
        return applyPagination(requestVo.getPageQueryParameters().getPageRequest(), query ->
                select(new QBoardPageVo_Element(
                        QBoard.board.id,
                        QBoard.board.hierarchy,
                        QBoard.board.boardType,
                        QBoard.board.contentType,
                        QBoard.board.viewCount,
                        QBoard.board.title,
                        QBoard.board.content
                        ))
                        .from(QBoard.board)
                        .orderBy(QBoard.board.hierarchy.commonParentId.desc(), QBoard.board.hierarchy.groupOrder.asc())
        );
    }

}
