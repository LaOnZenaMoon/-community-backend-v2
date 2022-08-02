package me.lozm.domain.board.code.converter;

import lombok.extern.slf4j.Slf4j;
import me.lozm.domain.board.code.BoardType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Slf4j
@Converter(autoApply = true)
public class BoardTypeConverter implements AttributeConverter<BoardType, String> {

    @Override
    public String convertToDatabaseColumn(BoardType attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.getCode();
    }

    @Override
    public BoardType convertToEntityAttribute(String dbData) {
        try {
            return BoardType.findCode(dbData);
        } catch (Exception e) {
            log.warn(String.format("DB로 부터 알수 없는 상태값을 받았습니다. => %s", dbData));
            return null;
        }
    }

}
