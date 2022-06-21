package me.lozm.global.code.converter;

import lombok.extern.slf4j.Slf4j;
import me.lozm.global.code.CommentType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Slf4j
@Converter(autoApply = true)
public class CommentTypeConverter implements AttributeConverter<CommentType, String> {

    @Override
    public String convertToDatabaseColumn(CommentType attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.getCode();
    }

    @Override
    public CommentType convertToEntityAttribute(String dbData) {
        try {
            return CommentType.findCode(dbData);
        } catch (Exception e) {
            log.warn(String.format("DB로 부터 알수 없는 상태값을 받았습니다. => %s", dbData));
            return null;
        }
    }

}
