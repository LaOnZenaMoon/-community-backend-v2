package me.lozm.domain.board.repository;

import me.lozm.domain.board.entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import javax.persistence.LockModeType;
import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long>, CommentRepositoryCustom {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Comment c WHERE c.id = :commentId")
    Optional<Comment> findByIdUsingLock(Long commentId);

    @Query("SELECT MAX(c.hierarchy.groupOrder) FROM Comment c " +
            "WHERE c.hierarchy.commonParentId = :commonParentId ")
    Optional<Integer> findMaxGroupOrder(Long commonParentId);

    @Query("SELECT MAX(c.hierarchy.groupOrder) FROM Comment c " +
            "WHERE c.hierarchy.commonParentId = :commonParentId " +
            "AND c.hierarchy.parentId = :parentId ")
    Optional<Integer> findMaxGroupOrder(Long commonParentId, Long parentId);

    List<Comment> findAllByHierarchy_CommonParentId(Long commonParentId);
}
