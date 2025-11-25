package com.epam.aix.estateassistant.service;

import com.epam.aix.estateassistant.downstream.AiAssistanceService;
import com.epam.aix.estateassistant.persistence.ChatRepository;
import com.epam.aix.estateassistant.persistence.entity.Chat;
import com.epam.aix.estateassistant.persistence.projection.ChatIdTitleProjection;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
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


    public String talk(String chatId, String message) {
        String response = aiAssistanceService.getResponse(chatId, message);
        log.info("AI Assistance response for chatId {}: {}", chatId, response);
        return response;
    }
}
