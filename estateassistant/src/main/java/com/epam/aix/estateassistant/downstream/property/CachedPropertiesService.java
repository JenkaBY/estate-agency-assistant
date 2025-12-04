package com.epam.aix.estateassistant.downstream.property;

import com.epam.aix.estateassistant.downstream.property.model.Property;
import com.epam.aix.estateassistant.service.dto.UserPropertiesAttributes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.CollectionUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
@RequiredArgsConstructor
public class CachedPropertiesService implements PropertiesService {

    private final PropertiesService delegate;
    private final Map<CacheKey, List<Property>> cache = new ConcurrentHashMap<>();

    @Override
    public Response apply(Request request) {
        if (CollectionUtils.isEmpty(request.searchAttributes())) {
            return new Response(Collections.emptyList());
        }
        var key = new CacheKey(Collections.unmodifiableList(request.searchAttributes()));
        log.info("Fetching properties for key {}", key.hashCode());
        return new Response(cache.computeIfAbsent(key, k -> {
            log.info("Creating cache for key {}", key.hashCode());
            return this.delegate.apply(request).properties();
        }));
    }

    record CacheKey(List<UserPropertiesAttributes> key) {
    }
}
