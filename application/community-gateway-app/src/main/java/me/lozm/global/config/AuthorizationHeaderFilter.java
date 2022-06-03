package me.lozm.global.config;

import io.jsonwebtoken.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthorizationHeaderFilter extends AbstractGatewayFilterFactory<AuthorizationHeaderFilter.Config> {

    private Environment environment;


    public static class Config {

    }


    public AuthorizationHeaderFilter(Environment environment) {
        super(Config.class);
        this.environment = environment;
    }


    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            ServerHttpRequest request = exchange.getRequest();

            if (!request.getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return onError(exchange, "No authorization header", HttpStatus.UNAUTHORIZED);
            }

            String authorizationHeader = request.getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
            String jwt = authorizationHeader.replace("Bearer", "");

            if (!isJwtValid(jwt)) {
                return onError(exchange, "JWT is not valid", HttpStatus.UNAUTHORIZED);
            }

            return chain.filter(exchange);
        };
    }

    private boolean isJwtValid(String jwt) {
        boolean returnValue = true;

        String subject = null;
        try {
            subject = Jwts.parser()
                    .setSigningKey(environment.getProperty("jwt-token.secret-key"))
                    .parseClaimsJws(jwt)
                    .getBody()
                    .getSubject();
        } catch (ExpiredJwtException e) {
            log.error(e.getMessage());
            returnValue = false;
        } catch (UnsupportedJwtException e) {
            log.error(e.getMessage());
            returnValue = false;
        } catch (MalformedJwtException e) {
            log.error(e.getMessage());
            returnValue = false;
        } catch (SignatureException e) {
            log.error(e.getMessage());
            returnValue = false;
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            returnValue = false;
        }

        if (StringUtils.isBlank(subject)) {
            return false;
        }

        return returnValue;
    }

    private Mono<Void> onError(ServerWebExchange serverWebExchange, String message, HttpStatus httpStatus) {
        ServerHttpResponse response = serverWebExchange.getResponse();
        response.setStatusCode(httpStatus);

        log.error(message);
        return response.setComplete();
    }

}
