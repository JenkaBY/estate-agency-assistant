package com.epam.aix.estateassistant.downstream.property;

import com.epam.aix.estateassistant.downstream.property.model.Property;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class FakePropertiesService implements PropertiesService {

    private final ChatClient propertiesGeneratorClient;
    private final ObjectMapper objectMapper;

    @Override
    @SneakyThrows
    public Response apply(Request request) {
        log.info("Generating fake properties list for search attributes: {}", request.searchAttributes());
        List<Property> aiGenEntities = propertiesGeneratorClient.prompt()
                .user(objectMapper.writeValueAsString(request.searchAttributes()))
                .call()
                .entity(PropertiesService.RESPONSE_TYPE_REF);
        log.info("Found properties for search attributes: {}", aiGenEntities);
        return new Response(aiGenEntities);
    }
}
