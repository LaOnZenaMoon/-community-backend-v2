package me.lozm.global.exception;

import lombok.extern.slf4j.Slf4j;
import me.lozm.utils.exception.CustomExceptionType;
import me.lozm.utils.exception.DateUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.common.exceptions.OAuth2Exception;
import org.springframework.security.oauth2.provider.error.DefaultWebResponseExceptionTranslator;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CustomOAuth2ExceptionTranslator extends DefaultWebResponseExceptionTranslator {

    @Override
    public ResponseEntity<OAuth2Exception> translate(Exception e) {
        log.info("message :: " + e.getMessage());
        CustomExceptionType exceptionType = CustomExceptionType.getOAuth2Exception(e.getMessage());

        Map<String, Object> response = new HashMap<>();
        response.put("code", exceptionType.getCode());
        response.put("message", exceptionType.getMessage());
        response.put("timestamp", LocalDateTime.now().format(DateUtils.DATE_TIME_FORMATTER));

        return new ResponseEntity(response, HttpStatus.UNAUTHORIZED);
    }
}
