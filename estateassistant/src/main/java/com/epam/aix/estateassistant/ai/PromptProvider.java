package com.epam.aix.estateassistant.ai;


import com.epam.aix.estateassistant.service.dto.ParameterType;
import com.epam.aix.estateassistant.service.dto.ParameterValueType;
import com.epam.aix.estateassistant.service.dto.PropertiesSearchParameter;
import com.epam.aix.estateassistant.service.dto.PropertiesSearchParameters;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class PromptProvider {

    private static final boolean MANDATORY = true;
    private static final boolean OPTIONAL = false;

    private static final PropertiesSearchParameters DEFAULT_SEARCH_PARAMETERS =
            new PropertiesSearchParameters(List.of(
                    PropertiesSearchParameter.of(ParameterType.LOCATION, MANDATORY),
                    PropertiesSearchParameter.of(ParameterType.PRICE, MANDATORY),
                    PropertiesSearchParameter.of(ParameterType.PROPERTY_TYPE, MANDATORY),
                    PropertiesSearchParameter.of(ParameterType.AMENITIES, OPTIONAL),
                    PropertiesSearchParameter.of(ParameterType.ADDITIONAL_FEATURES, OPTIONAL),
                    PropertiesSearchParameter.of(ParameterType.SIZE, OPTIONAL),
                    PropertiesSearchParameter.of(ParameterType.NUMBER_OF_ROOMS, OPTIONAL)
            ));

    private static final List<String> DEFAULT_FAKE_PROPERTIES_PARAMETERS =
            Arrays.stream(ParameterType.values())
                    .map(ParameterType::toString)
                    .toList();

    private static final String DEFAULT_SEARCH_PARAMETERS_VAR = "default_search_parameters";
    private static final String DEFAULT_PROPERTIES_ATTRIBUTES_VAR = "default_properties_attributes";
    private static final String USER_SEARCH_REQUEST_OUTPUT_STRUCTURE_VAR = "user_search_request_output_structure";
    private static final String PROPERTIES_DATA_OUTPUT_STRUCTURE_VAR = "properties_data_output_structure";

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
                    """.formatted(Arrays.toString(ParameterType.values()), Arrays.asList(ParameterValueType.values()));

    @Value("${app.chatbot.model}")
    private final String chatBotModel;

    @Value("${app.properties-generator.model}")
    private final String generatorModel;

    private static String getChatSystemPrompt() {
        return """
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
    }


    public Prompt getSystemPromptForAgent() {
        return SystemPromptTemplate.builder()
                .template(PromptProvider.getChatSystemPrompt())
                .variables(
                        Map.of(
                                DEFAULT_SEARCH_PARAMETERS_VAR, DEFAULT_SEARCH_PARAMETERS,
                                USER_SEARCH_REQUEST_OUTPUT_STRUCTURE_VAR, USER_SEARCH_REQUEST_OUTPUT_STRUCTURE)
                )
                .build().create(
                        ChatOptions.builder()
                                .model(chatBotModel)
                                .temperature(1.0)
                                .build()
                );
    }

    public Prompt getPropertiesDataGeneratorPrompt() {
        log.info(System.lineSeparator() + FAKE_PROPERTIES_DATA_GENERATOR_SYSTEM_PROMPT);
        return SystemPromptTemplate.builder()
                .template(FAKE_PROPERTIES_DATA_GENERATOR_SYSTEM_PROMPT)
                .variables(Map.of(
                        DEFAULT_PROPERTIES_ATTRIBUTES_VAR, DEFAULT_FAKE_PROPERTIES_PARAMETERS,
                        PROPERTIES_DATA_OUTPUT_STRUCTURE_VAR, PROPERTIES_DATA_OUTPUT_STRUCTURE
                ))
                .build().create(
                        ChatOptions.builder()
                                .model(generatorModel)
                                .temperature(1.0)
                                .build()
                );
    }

    private static final String PROPERTIES_DATA_OUTPUT_STRUCTURE =
            // language=JSON
            """
                    {
                      "id": "unique-property-id",
                      "title": "Charming 3-Bedroom House in Suburban Area",
                      "description": "A beautiful 3-bedroom, 2-bathroom house located in a quiet suburban neighborhood. Features a spacious living room, modern kitchen, and a large backyard.",
                      "location": {
                        "address": "1234 Elm Street",
                        "city": "Paris",
                        "neighborhood": "Sunnyvale",
                        "country": "France"
                      },
                      "price": 350000,
                      "size_sqm": 120,
                      "number_of_rooms": {
                        "bedrooms": 3,
                        "bathrooms": 2
                      },
                      "property_type": "House",
                      "amenities": ["Garage", "Garden", "Fireplace"],
                      "additional_features": ["Closest school is 5 km away", "Public bus stop in 5 minutes walking distance", "Nearby park with playground"],
                      "year_built": 2010,
                      "listing_date": "2024-01-15",
                      "card_link": "http://www.fake-estate-agency.com/properties/unique-property-id"
                    """;

    private static final String FAKE_PROPERTIES_DATA_GENERATOR_SYSTEM_PROMPT = """
            You are a data generator that creates fake real estate property listings for testing purposes.
            Generate realistic and diverse property data of 3 records including the attributes described below:
            ```
            {%s}
            ```.
            Ensure the generated data is coherent and plausible, reflecting real-world real estate market trends.
            The data should be structured in json format:
             ```
             {%s}
             ```
            Be creative and add details about see sights, parks and other interesting places nearby.
            Avoid using any real personal information or addresses except countries and cities; all data must be entirely fictional.
            """.formatted(DEFAULT_PROPERTIES_ATTRIBUTES_VAR, PROPERTIES_DATA_OUTPUT_STRUCTURE_VAR);

}
