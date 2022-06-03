package me.lozm.global.config;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import static java.lang.String.format;

@Slf4j
@Component
public class GlobalFilter extends AbstractGatewayFilterFactory<GlobalFilter.Config> {

    @Data
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;
    }

    public GlobalFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            final ServerHttpRequest request = exchange.getRequest();
            final ServerHttpResponse response = exchange.getResponse();

            log.info(format("Global filter Request ID: %s", request.getId()));
            if (config.isPreLogger()) {
                log.info(format("Request ID[%s]: URI -> %s", request.getId(), request.getURI()));
                log.info(format("Request ID[%s]: HEADERS -> %s", request.getId(), request.getHeaders()));
                //TODO Request Body 영역 logging 처리
                log.info(format("Request ID[%s]: BODY -> %s", request.getId(), request.getBody()));
            }

            return chain.filter(exchange)
                    .then(Mono.fromRunnable(() -> {
                        if (config.isPostLogger()) {
                            log.info(format("Response ID[%s]: HEADERS -> %s", request.getId(), response.getHeaders()));
                            log.info(format("Response ID[%s]: STATUS CODE -> %s", request.getId(), response.getStatusCode()));
                            //TODO Response Body 영역 logging 처리
                            log.info(format("Global filter Response ID: %s", request.getId()));
                        }
                    }));
        };
    }

}
