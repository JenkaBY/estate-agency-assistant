package com.epam.aix.estateassistant.service;

import com.epam.aix.estateassistant.downstream.AiAssistanceService;
import com.epam.aix.estateassistant.persistence.entity.Chat;
import com.epam.aix.estateassistant.persistence.ChatRepository;
import com.epam.aix.estateassistant.persistence.projection.ChatIdTitleProjection;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class ChatService {

    private final AiAssistanceService aiAssistanceService;

    private final ChatRepository chatRepository;

    @Value("${app.estate-agency.name}")
    private final String agencyName;

    public Chat findChatById(String chatId) {
        return chatRepository.findById(chatId).orElse(Chat.builder()
                        .id(chatId)
                        .title("%s's Estate Assistant".formatted(agencyName))
                        .messages(List.of())
                .build());
    }

    public List<ChatIdTitleProjection> getAllChats() {
        return chatRepository.findAllChatIdsAndTitles();
    }



}
