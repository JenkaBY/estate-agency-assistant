package com.epam.aix.estateassistant.config.prompt;

import com.epam.aix.estateassistant.service.dto.ParameterType;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class FakeRealEstateApiPromptProvider implements PromptProvider {

    private static final String DEFAULT_PROPERTIES_ATTRIBUTES_VAR = "default_properties_attributes";

    private static final List<String> DEFAULT_FAKE_PROPERTIES_PARAMETERS =
            Arrays.stream(ParameterType.values())
                    .map(ParameterType::toString)
                    .toList();

    private static final String FAKE_PROPERTIES_DATA_GENERATOR_SYSTEM_PROMPT = """
            Generate 2 realistic real estate property listings as JSON with these attributes:
            ```
            {%s}
            ```
            Requirements:
            - Match search criteria from user prompts
            - Use fictional data only (real countries/cities allowed)
            - Include nearby attractions (parks, sights, etc.)
            """.formatted(DEFAULT_PROPERTIES_ATTRIBUTES_VAR);

    @Override
    public Prompt getSystemPrompt() {
        return SystemPromptTemplate.builder()
                .template(FAKE_PROPERTIES_DATA_GENERATOR_SYSTEM_PROMPT)
                .variables(Map.of(
                        DEFAULT_PROPERTIES_ATTRIBUTES_VAR, DEFAULT_FAKE_PROPERTIES_PARAMETERS
                ))
                .build().create();
    }
}
