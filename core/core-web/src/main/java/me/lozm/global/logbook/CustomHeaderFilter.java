package me.lozm.global.logbook;

import org.zalando.logbook.HeaderFilter;
import org.zalando.logbook.Headers;

import java.util.List;
import java.util.Map;
import java.util.Set;

public class CustomHeaderFilter implements HeaderFilter {

    private Set<String> includes;

    public CustomHeaderFilter(Set<String> includes) {
        this.includes = includes;
    }

    @Override
    public Map<String, List<String>> filter(Map<String, List<String>> headers) {

        final Map<String, List<String>> result = Headers.empty();

        headers.forEach((name, values) -> {
            if(includes.contains(name))
                result.put(name, values);
        });

        return Headers.immutableCopy(result);
    }
}
