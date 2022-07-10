package me.lozm.app.board.service;

import lombok.RequiredArgsConstructor;
import me.lozm.domain.board.entity.Board;
import me.lozm.domain.board.entity.Comment;
import me.lozm.domain.board.mapper.BoardMapper;
import me.lozm.domain.board.repository.BoardRepository;
import me.lozm.domain.board.service.BoardHelperService;
import me.lozm.domain.board.vo.BoardCreateVo;
import me.lozm.domain.board.vo.BoardDetailVo;
import me.lozm.domain.board.vo.BoardPageVo;
import me.lozm.domain.board.vo.BoardUpdateVo;
import me.lozm.global.model.HierarchyRequestAble;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import static java.lang.String.format;


@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class BoardServiceImpl extends HierarchyService<Board> implements BoardService {

    private final BoardRepository boardRepository;
    private final BoardHelperService boardHelperService;
    private final BoardMapper boardMapper;


    @Override
    public Page<BoardPageVo.Element> getBoards(BoardPageVo.Request requestVo) {
        return boardRepository.findBoards(requestVo);
    }

    @Override
    @Transactional
    public BoardDetailVo.Response getBoardDetail(Long boardId) {
        Board board = boardHelperService.getBoardUsingLock(boardId);
        board.addViewCount();
        return boardMapper.toDetailVo(board);
    }

    @Override
    @Transactional
    public BoardDetailVo.Response createBoard(BoardCreateVo.Request requestVo) {
        Function<BoardCreateVo.Request, Board> saveEntityFunction =
                request -> boardRepository.save(Board.from(requestVo));
        return createEntity(requestVo, saveEntityFunction);
    }

    @Override
    @Transactional
    public BoardDetailVo.Response updateBoard(BoardUpdateVo.Request boardUpdateVo) {
        Board board = boardHelperService.getBoardUsingLock(boardUpdateVo.getBoardId());
        board.update(boardUpdateVo);
        return boardMapper.toDetailVo(board);
    }

    @Override
    @Transactional
    public void deleteBoard(Long boardId) {
        Board board = boardHelperService.getBoardUsingLock(boardId);
        board.updateIsUse(false);
        for (Comment comment : board.getComments()) {
            comment.updateIsUse(false);
        }
    }

    @Override
    protected <T extends HierarchyRequestAble> void updateEntityWhenHierarchyTypeIsOrigin(Board entity, T request) {
        entity.getHierarchy().update(entity.getId());
    }

    @Override
    protected <T extends HierarchyRequestAble> void updateEntityWhenHierarchyTypeIsReplyForOrigin(Board entity, T request) {
        final Long parentId = request.getParentId();
        Board parentBoard = boardHelperService.getBoard(parentId);
        Integer maxGroupOrder = getMaxGroupOrderWhenReplyForOrigin(parentId);
        entity.getHierarchy().update(parentId,
                parentId,
                parentBoard.getHierarchy().getGroupLayer() + 1,
                maxGroupOrder + 1
        );
    }

    @Override
    protected <T extends HierarchyRequestAble> void updateEntityWhenHierarchyTypeIsReplyForReply(Board entity, T request) {
        final Long parentId = request.getParentId();
        Board parentBoard = boardHelperService.getBoard(parentId);
        Integer maxGroupOrder = getMaxGroupOrderWhenReplyForReply(parentBoard);
        entity.getHierarchy().update(
                parentBoard.getHierarchy().getCommonParentId(),
                parentId,
                parentBoard.getHierarchy().getGroupLayer() + 1,
                maxGroupOrder + 1
        );
        increaseBoardsGroupOrderBehindCreatedBoard(parentBoard, maxGroupOrder);
    }

    @Override
    protected <R> R createResponse(Board entity) {
        return (R) boardMapper.toDetailVo(entity);
    }

    private Integer getMaxGroupOrderWhenReplyForOrigin(Long parentBoardId) {
        return boardRepository.findMaxGroupOrder(parentBoardId)
                .orElseThrow(() -> new IllegalStateException(format("groupOrder 처리에 오류가 발생하였습니다. parentBoardId: %d", parentBoardId)));
    }

    private void increaseBoardsGroupOrderBehindCreatedBoard(Board parentBoard, Integer maxGroupOrder) {
        List<Board> boardList = boardRepository.findAllByHierarchy_CommonParentId(parentBoard.getHierarchy().getCommonParentId());
        for (Board board : boardList) {
            if (board.getHierarchy().getGroupOrder() > maxGroupOrder + 1) {
                board.getHierarchy().increaseGroupOrder();
            }
        }
    }

    private Integer getMaxGroupOrderWhenReplyForReply(Board parentBoard) {
        final Long commonParentId = parentBoard.getHierarchy().getCommonParentId();
        final Long parentId = parentBoard.getHierarchy().getParentId();

        Optional<Integer> maxGroupOrderOptional1 = boardRepository.findMaxGroupOrder(commonParentId, parentBoard.getId());
        if (maxGroupOrderOptional1.isPresent()) {
            return maxGroupOrderOptional1.get();
        }

        Optional<Integer> maxGroupOrderOptional2 = boardRepository.findMaxGroupOrder(commonParentId, parentId);
        if (maxGroupOrderOptional2.isPresent()) {
            return maxGroupOrderOptional2.get();
        }

        throw new IllegalStateException(format("게시글 groupOrder 처리에 오류가 발생하였습니다. commonParentId: %d, parentId: %d"
                , commonParentId, parentId));
    }

}
