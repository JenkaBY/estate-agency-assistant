package com.epam.aix.estateassistant.config.prompt;

import com.epam.aix.estateassistant.service.dto.ParameterType;
import com.epam.aix.estateassistant.service.dto.PropertiesSearchParameter;
import com.epam.aix.estateassistant.service.dto.PropertiesSearchParameters;
import lombok.experimental.UtilityClass;

import java.util.Arrays;
import java.util.List;

@UtilityClass
class PromptUtils {

    private static final boolean MANDATORY = true;
    private static final boolean OPTIONAL = false;

    public static final PropertiesSearchParameters DEFAULT_SEARCH_PARAMETERS =
            new PropertiesSearchParameters(List.of(
                    PropertiesSearchParameter.of(ParameterType.SERVICE_TYPE, MANDATORY),
                    PropertiesSearchParameter.of(ParameterType.LOCATION, MANDATORY),
                    PropertiesSearchParameter.of(ParameterType.PRICE, MANDATORY),
                    PropertiesSearchParameter.of(ParameterType.PROPERTY_TYPE, MANDATORY),
                    PropertiesSearchParameter.of(ParameterType.AMENITIES, OPTIONAL),
                    PropertiesSearchParameter.of(ParameterType.ADDITIONAL_FEATURES, OPTIONAL),
                    PropertiesSearchParameter.of(ParameterType.SIZE, OPTIONAL),
                    PropertiesSearchParameter.of(ParameterType.NUMBER_OF_ROOMS, OPTIONAL)
            ));

    static String getOptions(Enum<?> enumType) {
        return String.join("|", Arrays.stream(enumType.getClass().getEnumConstants())
                .map(Enum::name)
                .toList());
    }
}
