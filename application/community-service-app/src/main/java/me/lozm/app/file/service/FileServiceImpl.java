package me.lozm.app.file.service;

import lombok.RequiredArgsConstructor;
import me.lozm.domain.board.service.BoardHelperService;
import me.lozm.domain.board.service.CommentHelperService;
import me.lozm.domain.file.code.FileUploadType;
import me.lozm.domain.file.entity.File;
import me.lozm.domain.file.repository.FileRepository;
import me.lozm.domain.file.vo.FileUploadVo;
import me.lozm.exception.BadRequestException;
import me.lozm.exception.CustomExceptionType;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final BoardHelperService boardHelperService;
    private final CommentHelperService commentHelperService;
    private final FileRepository fileRepository;


    @Override
    @Transactional
    public FileUploadVo.Response uploadFile(FileUploadVo.Request requestVo) {
        final FileUploadType uploadType = requestVo.getUploadType();
        final Long targetId = requestVo.getTargetId();

        if (uploadType == FileUploadType.BOARD) {
            boardHelperService.getBoard(targetId);
        } else if (uploadType == FileUploadType.COMMENT) {
            commentHelperService.getComment(targetId);
        } else {
            throw new BadRequestException(CustomExceptionType.INVALID_REQUEST_PARAMETERS);
        }

        File file = fileRepository.save(File.of(uploadType, targetId));

        return new FileUploadVo.Response(file.getId(), uploadType, targetId);
    }

}
