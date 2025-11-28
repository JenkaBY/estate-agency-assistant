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
            You are a data generator that creates fake real estate property listings for testing purposes.
            Generate realistic and diverse property data of 2 records including the attributes described below:
            ```
            {%s}
            ```.
            Ensure the generated data is coherent and plausible, reflecting real-world real estate market trends.
            The data should be structured in json according to the schema provided.
            Be creative and add details about see sights, parks and other interesting places nearby.
            Avoid using any real personal information or addresses except countries and cities; all data must be entirely fictional.
            The generated properties should fit to the search attributes provided in user prompts in json format.
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
