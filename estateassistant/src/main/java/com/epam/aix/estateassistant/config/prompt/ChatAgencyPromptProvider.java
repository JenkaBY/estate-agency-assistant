package com.epam.aix.estateassistant.config.prompt;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.stereotype.Component;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class ChatAgencyPromptProvider implements PromptProvider {

    private static final String DEFAULT_SEARCH_PARAMETERS_VAR = "default_search_parameters";

    private static final String CHAT_SYSTEM_PROMPT =
            """
                    You are a real estate assistant helping users find properties.
                    Provide concise information about buying and renting near the user's location.
                    Maintain a professional, friendly tone.
                    Ask clarifying questions to gather required search parameters:
                    ```
                    {%s}
                    ```
                    Structure outputs as JSON per the provided schema.
                    
                    Set `isAllDataCollected` to true when all mandatory parameters are collected. Include polite follow-up questions in `textResponse` for missing required parameters.
                    Once data is complete, ALWAYS call the real estate service to return matching properties in HTML format with links opening in new tabs. Never generate fake property data by yourself.
                    
                    Stay focused on real estate topics only. Don't share PII except phone numbers and property addresses.
                    If no properties match, suggest leaving contact information for future notifications.
                    """.formatted(DEFAULT_SEARCH_PARAMETERS_VAR);

    @Override
    public Prompt getSystemPrompt() {
        log.debug("{}{}", System.lineSeparator(), CHAT_SYSTEM_PROMPT);

        return SystemPromptTemplate.builder()
                .template(CHAT_SYSTEM_PROMPT)
                .variables(
                        Map.of(
                                DEFAULT_SEARCH_PARAMETERS_VAR, PromptUtils.DEFAULT_SEARCH_PARAMETERS)
                )
                .build().create();
    }
}
