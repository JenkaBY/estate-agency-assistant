package com.epam.aix.estateassistant.downstream;

public interface AiAssistanceService {

    String generateWelcomeContent(String assistantName);

    String getResponse(String chatId, String message);
}
