package com.epam.aix.estateassistant.downstream;

import com.epam.aix.estateassistant.service.dto.UserGatheredPropertiesSearch;
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
        return ("Welcome to %s's Estate, your trusted partner in real estate." +
                " I'm here to help you with all your property needs, whether you're looking to rent or buy." +
                " Feel free to ask any questions!?").formatted(assistantName);
    }

    @Override
    public String getResponse(String chatId, String message) {
        return agentChatClient.prompt()
                .user(message)
                .advisors(advisor -> advisor.param(ChatMemory.CONVERSATION_ID, chatId))
                .call()
                .responseEntity(UserGatheredPropertiesSearch.class).entity().textResponse();
    }
}
