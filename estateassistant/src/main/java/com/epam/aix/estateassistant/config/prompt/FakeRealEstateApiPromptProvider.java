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
    private static final String PROPERTIES_DATA_OUTPUT_STRUCTURE_VAR = "properties_data_output_structure";

    private static final List<String> DEFAULT_FAKE_PROPERTIES_PARAMETERS =
            Arrays.stream(ParameterType.values())
                    .map(ParameterType::toString)
                    .toList();

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
                      "price": {
                        "amount": 350000,
                        "currency": "EUR"
                      },
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
                    }
                    """;

    @Override
    public Prompt getSystemPrompt() {
        return SystemPromptTemplate.builder()
                .template(FAKE_PROPERTIES_DATA_GENERATOR_SYSTEM_PROMPT)
                .variables(Map.of(
                        DEFAULT_PROPERTIES_ATTRIBUTES_VAR, DEFAULT_FAKE_PROPERTIES_PARAMETERS,
                        PROPERTIES_DATA_OUTPUT_STRUCTURE_VAR, PROPERTIES_DATA_OUTPUT_STRUCTURE
                ))
                .build().create();
    }
}
