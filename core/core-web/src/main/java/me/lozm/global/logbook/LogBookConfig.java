package me.lozm.global.logbook;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.zalando.logbook.DefaultSink;
import org.zalando.logbook.HeaderFilter;
import org.zalando.logbook.Sink;
import org.zalando.logbook.autoconfigure.LogbookProperties;

import java.util.Set;
import java.util.TreeSet;

@Configuration
@RequiredArgsConstructor
public class LogBookConfig {
    private final LogbookProperties properties;

    @Bean
    public Sink sink() {
        return new DefaultSink(new CustomLogFormatter(), new CustomLogWriter());
    }

    @Bean
    public HeaderFilter headerFilter() {
        final Set<String> headers = new TreeSet<>(String.CASE_INSENSITIVE_ORDER);
        headers.addAll(properties.getObfuscate().getHeaders());

        return new CustomHeaderFilter(headers);
    }
}
