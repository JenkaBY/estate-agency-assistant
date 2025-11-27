package com.epam.aix.estateassistant.config.prompt;

import com.epam.aix.estateassistant.service.dto.ParameterType;
import com.epam.aix.estateassistant.service.dto.ParameterValueType;
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
    private static final String USER_SEARCH_REQUEST_OUTPUT_STRUCTURE_VAR = "user_search_request_output_structure";

    private static final String CHAT_SYSTEM_PROMPT =
            """
                    You are a helpful real estate assistant to assist users with their real estate needs.
                    Provide accurate and concise information about properties, buying, and renting for nearby a location provided in user prompt.
                    Always maintain a professional and friendly tone.
                    Interact with users in a way that reflects the values and services of a reputable real estate agency.
                    Ask clarifying questions if needed to better understand user requirements until you can provide the best possible assistance and fulfill the following user search properties request parameters
                    ```
                    {%s}
                    ```
                    Gather data according to the provided search parameters. Outputs should be clear and structured in json format:
                    ```
                    {%s}
                    ```
                    Once mandatory data collected, set the `isAllDataCollected` field as true. The `textResponse`
                    must contain the follow up polite question about the missing search parameters if the search request mandatory parameters are still missing.
                    When all mandatory parameters are collected, provide a list of available properties that match the user's criteria.
                    
                    Answer only in the context of real estate and avoid unrelated topics. Don't provide any user PII data expect phone number and property address.
                    if the looking for a property is not found in available on the market list, suggest to leave a contact
                    to reach the user out when anything similar to the request show up on the database.
                    
                    User prompt:
                    """.formatted(DEFAULT_SEARCH_PARAMETERS_VAR, USER_SEARCH_REQUEST_OUTPUT_STRUCTURE_VAR);

    private static final String USER_SEARCH_REQUEST_OUTPUT_STRUCTURE =
            // language=JSON
            """
                    {
                    "isAllDataCollected": true/false,
                    "textResponse": "response text to the user",
                    "searchAttributes": [{
                        "parameter": {
                            "name": %s,
                            "mandatory":  true/false
                            },
                         "value": "user provided value",
                         "type": %s
                        } ]
                    }
                    """.formatted(PromptUtils.getOptions(ParameterType.PROPERTY_TYPE), PromptUtils.getOptions(ParameterValueType.EXACT)
            );

    @Override
    public Prompt getSystemPrompt() {
        log.info(System.lineSeparator() + CHAT_SYSTEM_PROMPT);

        return SystemPromptTemplate.builder()
                .template(CHAT_SYSTEM_PROMPT)
                .variables(
                        Map.of(
                                DEFAULT_SEARCH_PARAMETERS_VAR, PromptUtils.DEFAULT_SEARCH_PARAMETERS,
                                USER_SEARCH_REQUEST_OUTPUT_STRUCTURE_VAR, USER_SEARCH_REQUEST_OUTPUT_STRUCTURE)
                )
                .build().create();
    }
}
