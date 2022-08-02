package me.lozm.domain.file.code.converter;

import lombok.extern.slf4j.Slf4j;
import me.lozm.domain.file.code.FileUploadType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Slf4j
@Converter(autoApply = true)
public class FileUploadTypeConverter implements AttributeConverter<FileUploadType, String> {

    @Override
    public String convertToDatabaseColumn(FileUploadType attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.getCode();
    }

    @Override
    public FileUploadType convertToEntityAttribute(String dbData) {
        try {
            return FileUploadType.findCode(dbData);
        } catch (Exception e) {
            log.warn(String.format("DB로 부터 알수 없는 상태값을 받았습니다. => %s", dbData));
            return null;
        }
    }

}
