package me.lozm.domain.board.code.converter;

import lombok.extern.slf4j.Slf4j;
import me.lozm.domain.board.code.ContentType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Slf4j
@Converter(autoApply = true)
public class ContentTypeConverter implements AttributeConverter<ContentType, String> {

    @Override
    public String convertToDatabaseColumn(ContentType attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.getCode();
    }

    @Override
    public ContentType convertToEntityAttribute(String dbData) {
        try {
            return ContentType.findCode(dbData);
        } catch (Exception e) {
            log.warn(String.format("DB로 부터 알수 없는 상태값을 받았습니다. => %s", dbData));
            return null;
        }
    }

}
