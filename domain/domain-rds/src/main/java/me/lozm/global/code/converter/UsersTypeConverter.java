package me.lozm.global.code.converter;

import lombok.extern.slf4j.Slf4j;
import me.lozm.global.code.UsersType;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Slf4j
@Converter(autoApply = true)
public class UsersTypeConverter implements AttributeConverter<UsersType, Long> {

    private static final String ERROR_USE_YN_DB_VALUE = "DB로 부터 알수 없는 상태값을 받았습니다. => %s";

    @Override
    public Long convertToDatabaseColumn(UsersType attribute) {
        if (attribute == null) {
            return null;
        }

        return attribute.getCode();
    }

    @Override
    public UsersType convertToEntityAttribute(Long dbData) {
        try {
            return UsersType.findCode(dbData);
        } catch (Exception e) {
            log.warn(String.format(ERROR_USE_YN_DB_VALUE, dbData));
            return null;
        }
    }

}
