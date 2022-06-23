package me.lozm.global.auditor;

import io.jsonwebtoken.Jwt;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.AuditorAware;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Optional;

import static org.apache.commons.lang3.ObjectUtils.isEmpty;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuditorConfig implements AuditorAware<Long> {

    private final Environment environment;


    @Override
    public Optional<Long> getCurrentAuditor() {
        RequestAttributes requestAttributes = RequestContextHolder.currentRequestAttributes();
        Jwt jwt = getJwt((ServletRequestAttributes) requestAttributes);
        if (isEmpty(jwt)) {
            return Optional.of(-1L);
        }

        return Optional.ofNullable(JwtUtils.getUserIdFromJwt(jwt));
    }

    private Jwt getJwt(ServletRequestAttributes requestAttributes) {
        try {
            String jwt = requestAttributes.getRequest().getHeader(HttpHeaders.AUTHORIZATION);
            return JwtUtils.getJwtObject(jwt, environment.getProperty("security.oauth2.jwt.sign-key"));
        } catch (RuntimeException e) {
            log.error(e.getMessage());
        }

        return null;
    }

}
