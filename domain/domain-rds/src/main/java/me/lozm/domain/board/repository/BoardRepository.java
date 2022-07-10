package me.lozm.domain.board.repository;

import me.lozm.domain.board.entity.Board;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface BoardRepository extends JpaRepository<Board, Long>, BoardRepositoryCustom {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Board b WHERE b.id = :boardId")
    Optional<Board> findByIdUsingLock(Long boardId);

    @Query("SELECT MAX(b.hierarchy.groupOrder) FROM Board b " +
            "WHERE b.hierarchy.commonParentId = :commonParentId ")
    Optional<Integer> findMaxGroupOrder(Long commonParentId);

    @Query("SELECT MAX(b.hierarchy.groupOrder) FROM Board b " +
            "WHERE b.hierarchy.commonParentId = :commonParentId " +
            "AND b.hierarchy.parentId = :parentId ")
    Optional<Integer> findMaxGroupOrder(Long commonParentId, Long parentId);

    List<Board> findAllByHierarchy_CommonParentId(Long commonParentId);
}
