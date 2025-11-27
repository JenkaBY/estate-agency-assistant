package com.epam.aix.estateassistant.config.prompt;

import org.springframework.ai.chat.prompt.Prompt;

public interface PromptProvider {

    Prompt getSystemPrompt();
}
