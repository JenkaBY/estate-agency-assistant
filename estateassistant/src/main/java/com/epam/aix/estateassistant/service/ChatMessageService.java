package com.epam.aix.estateassistant.service;

import com.epam.aix.estateassistant.downstream.AiAssistanceService;
import com.epam.aix.estateassistant.persistence.entity.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class ChatMessageService {

    private final AiAssistanceService assistanceService;

    public ChatMessage generateWelcomeMessage(String chatId, String assistantName){
        var content = assistanceService.generateWelcomeContent(assistantName);
        return ChatMessage.builder()
                .sender(assistantName)
                .chatId(chatId)
                .messageId(UUID.randomUUID().toString())
                .content(content)
                .build();
    }
}
