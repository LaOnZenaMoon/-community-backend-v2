package me.lozm.domain.file.service;

import lombok.RequiredArgsConstructor;
import me.lozm.domain.file.entity.File;
import me.lozm.domain.file.repository.FileRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static java.lang.String.format;

@Service
@RequiredArgsConstructor
public class FileHelperService {

    private final FileRepository fileRepository;


    public Optional<File> findFile(String fileId) {
        return fileRepository.findById(fileId);
    }

    public File getFile(String fileId) {
        return findFile(fileId).orElseThrow(() -> new IllegalArgumentException(getNotFoundFormat(fileId)));
    }

    private String getNotFoundFormat(String fileId) {
        return format("존재하지 않는 파일입니다. 파일 ID: %s", fileId);
    }

}
