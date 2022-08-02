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
@SequenceGenerator(name = "FILE_SEQ_GEN", sequenceName = "FILE_SEQ")
public class File extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FILE_SEQ_GEN")
    @Column(name = "FILE_ID")
    private Long id;

    @Column(name = "UPLOAD_TYPE")
    @Convert(converter = FileUploadTypeConverter.class)
    private FileUploadType uploadType;

    @Column(name = "TARGET_ID")
    private Long targetId;

    public static File of(FileUploadType uploadType, Long targetId) {
        File file = new File();
        file.isUse = true;
        file.uploadType = uploadType;
        file.targetId = targetId;
        return file;
    }

}
