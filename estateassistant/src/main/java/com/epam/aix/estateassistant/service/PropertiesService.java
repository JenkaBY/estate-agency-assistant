package com.epam.aix.estateassistant.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@Slf4j
@RequiredArgsConstructor
@Service
public class PropertiesService {

    private final ChatClient propertiesGeneratorClient;

    public String getPropertiesForLocation(String location) {

        return propertiesGeneratorClient.prompt()
                .user(location)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, location))
                .call()
                .content();
    }
}
