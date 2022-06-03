package me.lozm.global.logbook;

import com.fasterxml.jackson.annotation.JsonRawValue;
import com.fasterxml.jackson.annotation.JsonValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import org.zalando.logbook.*;

import java.io.IOException;
import java.util.*;
import java.util.function.Predicate;

public class CustomLogFormatter implements HttpLogFormatter {
    private final ObjectMapper mapper;

    public CustomLogFormatter() {
        this(new ObjectMapper());
    }
    public CustomLogFormatter(final ObjectMapper mapper) {
        this.mapper = mapper;
    }

    @Override
    public String format(Precorrelation precorrelation, HttpRequest request) throws IOException {

        return format(prepare(precorrelation, request));
    }

    @Override
    public String format(Correlation correlation, HttpResponse response) throws IOException {

        return format(prepare(correlation, response));
    }

    public String format(final Map<String, Object> content) throws IOException {
        return mapper.writerWithDefaultPrettyPrinter().writeValueAsString(content);
    }

    private Map<String, Object> prepare(final Precorrelation precorrelation, final HttpRequest request) throws IOException {

        final Map<String, Object> content = new LinkedHashMap<>();

        content.put("origin", request.getOrigin().name().toLowerCase(Locale.ROOT));
        content.put("type", "request");
        content.put("correlation", precorrelation.getId());
        content.put("remote", request.getRemote());
        content.put("method", request.getMethod());
        content.put("uri", request.getRequestUri());

        prepareHeaders(request).ifPresent(headers -> content.put("headers", headers));
        if (request.getHeaders().containsKey("content-type")) {
            if (request.getHeaders().get("content-type").stream().filter(header -> header.contains("multipart")).count() == 0) {
                prepareBody(request).ifPresent(body -> content.put("body", body));
            }
        }

        return content;
    }

    private Map<String, Object> prepare(final Correlation correlation, final HttpResponse response) throws IOException {

        final Map<String, Object> content = new LinkedHashMap<>();

        content.put("origin", response.getOrigin().name().toLowerCase(Locale.ROOT));
        content.put("type", "response");
        content.put("correlation", correlation.getId());
        content.put("duration", correlation.getDuration().toMillis());
        content.put("status", response.getStatus());

        prepareHeaders(response).ifPresent(headers -> content.put("headers", headers));
        if (response.getHeaders().containsKey("content-type")) {
            if (response.getHeaders().get("content-type").stream().filter(header -> header.contains("json")).count() > 0) {
                prepareBody(response).ifPresent(body -> content.put("body", body));
            }
        }

        return content;
    }

    private Optional<Map<String, List<String>>> prepareHeaders(final HttpMessage message) {
        final Map<String, List<String>> headers = message.getHeaders();
        return Optional.ofNullable(headers.isEmpty() ? null : headers);
    }

    private Optional<Object> prepareBody(final HttpMessage message) throws IOException {

        final String contentType = message.getContentType();
        final String body = new String(message.getBody(),"utf-8" );// message.getBodyAsString();
        if(body.isEmpty()) {
            return Optional.empty();
        }
        if (JsonMediaType.JSON.test(contentType)) {
            // TODO has this JSON been validated? If not then this might result in invalid log statements
            return Optional.of(new JsonBody(body));
        } else {
            return Optional.of(body);
        }
    }

    @AllArgsConstructor
    private static final class JsonBody {
        String json;

        @JsonRawValue
        @JsonValue
        public String getJson() {
            return json;
        }
    }

    private static final class JsonMediaType {

        private JsonMediaType() {
        }

        static final Predicate<String> JSON = contentType -> {
            if(contentType == null) {
                return false;
            }
            // implementation note: manually coded for improved performance
            if(contentType.startsWith("application/")) {
                int index = contentType.indexOf(';', 12);
                if(index != -1) {
                    if(index > 16) {
                        // application/some+json;charset=utf-8
                        return contentType.regionMatches(index - 5, "+json", 0, 5);
                    }

                    // application/json;charset=utf-8
                    return contentType.regionMatches(index - 4, "json", 0, 4);
                } else {
                    // application/json
                    if(contentType.length() == 16) {
                        return contentType.endsWith("json");
                    }
                    // application/some+json
                    return contentType.endsWith("+json");
                }
            }
            return false;
        };
    }
}
