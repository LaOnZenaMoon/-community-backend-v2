package me.lozm.domain.file.entity;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import me.lozm.domain.file.code.FileUploadType;
import me.lozm.domain.file.code.converter.FileUploadTypeConverter;
import me.lozm.global.model.entity.BaseEntity;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.hibernate.annotations.Where;

import javax.persistence.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PROTECTED)

@Entity
@Table(name = "FILE")
@Where(clause = "IS_USE = true")
@DynamicUpdate
@DynamicInsert
public class File extends BaseEntity {

    @Id
    @Column(name = "FILE_ID")
    private String id;

    @Column(name = "FILE_NAME")
    private String fileName;

    @Column(name = "FILE_EXTENSION")
    private String fileExtension;

    @Column(name = "UPLOAD_TYPE")
    @Convert(converter = FileUploadTypeConverter.class)
    private FileUploadType uploadType;

    @Column(name = "TARGET_ID")
    private Long targetId;

    public static File of(String randomFileName, String originalFileName, String fileExtension, FileUploadType uploadType, Long targetId) {
        File file = new File();
        file.isUse = true;
        file.id = randomFileName;
        file.fileName = originalFileName;
        file.fileExtension = fileExtension;
        file.uploadType = uploadType;
        file.targetId = targetId;
        return file;
    }

}
