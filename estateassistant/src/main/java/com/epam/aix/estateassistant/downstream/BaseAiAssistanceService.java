package com.epam.aix.estateassistant.downstream;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class BaseAiAssistanceService implements AiAssistanceService {

    private final ChatClient agentChatClient;

    @Override
    public String generateWelcomeContent(String assistantName) {
        return "Welcome to %s's Estate Assistant! How can I assist you today?".formatted(assistantName);
    }

    @Override
    public String getResponse(String chatId, String message) {
        return agentChatClient.prompt()
                .user(message)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .content();
    }
}
