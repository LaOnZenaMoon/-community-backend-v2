package me.lozm.domain.file.repository;

import me.lozm.domain.file.entity.File;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<File, String> {
    Optional<File> findByTargetId(Long boardId);
}
