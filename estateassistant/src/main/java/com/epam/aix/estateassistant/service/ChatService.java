package com.epam.aix.estateassistant.service;

import com.epam.aix.estateassistant.downstream.AiAssistanceService;
import com.epam.aix.estateassistant.service.dto.MessageDto;
import com.epam.aix.estateassistant.service.dto.UserGatheredPropertiesSearch;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.memory.ChatMemoryRepository;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class ChatService {

    private final AiAssistanceService aiAssistanceService;

    private final ChatMemoryRepository customChatMemoryRepository;
    private final ObjectMapper objectMapper;

    @Value("${app.estate-agency.name}")
    private final String agencyName;

    public List<MessageDto> findChatById(String chatId) {
        List<Message> messages = customChatMemoryRepository.findByConversationId(chatId);
        if (messages.isEmpty()) {
            customChatMemoryRepository.saveAll(chatId, List.of(getWelcomeMessage()));
            messages = customChatMemoryRepository.findByConversationId(chatId);
        }

        return messages.stream()
                .map(this::toDto)
                .toList();
    }

    public String talk(String chatId, String message) {
        var response = aiAssistanceService.getResponse(chatId, message);
        log.info("AI Assistance response for chatId {}: {}", chatId, response);
        return response;
    }

    @SneakyThrows
    private AssistantMessage getWelcomeMessage() {
        return new AssistantMessage(
                objectMapper.writeValueAsString(UserGatheredPropertiesSearch.builder()
                        .textResponse(aiAssistanceService.generateWelcomeContent(agencyName))
                        .build())
        );
    }

    @SneakyThrows
    private MessageDto toDto(Message input) {
        if (input instanceof AssistantMessage assistantMessage && assistantMessage.getText() != null) {
            var content = objectMapper.readValue(assistantMessage.getText(), UserGatheredPropertiesSearch.class).textResponse();
            return new MessageDto(input.getMessageType(), content);
        }
        return new MessageDto(input.getMessageType(), Optional.ofNullable(input.getText()).orElse("NA"));
    }
}
