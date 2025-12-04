package com.epam.aix.estateassistant.config;

import com.epam.aix.estateassistant.downstream.property.CachedPropertiesService;
import com.epam.aix.estateassistant.downstream.property.FakePropertiesService;
import com.epam.aix.estateassistant.downstream.property.PropertiesService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PropertyServiceConfig {

    @Bean("realEstateService")
    public PropertiesService realEstateService(ChatClient propertiesGeneratorClient, ObjectMapper objectMapper) {
        return new CachedPropertiesService(new FakePropertiesService(propertiesGeneratorClient, objectMapper));
    }

}
