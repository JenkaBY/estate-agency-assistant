package com.epam.aix.estateassistant.downstream;

import org.springframework.stereotype.Service;

@Service
public class BaseAiAssistanceService implements AiAssistanceService {

    @Override
    public String generateWelcomeContent(String assistantName) {
        return "Welcome to %s's Estate Assistant! How can I assist you today?".formatted(assistantName);
    }
}
