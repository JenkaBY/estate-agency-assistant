package com.epam.aix.estateassistant.downstream.property;

import com.epam.aix.estateassistant.downstream.property.model.Property;
import com.epam.aix.estateassistant.service.dto.UserPropertiesAttributes;
import org.springframework.core.ParameterizedTypeReference;

import java.util.List;
import java.util.function.Function;

public interface PropertiesService extends Function<PropertiesService.Request, PropertiesService.Response> {

    ParameterizedTypeReference<List<Property>> RESPONSE_TYPE_REF = new ParameterizedTypeReference<>() {
    };

    @Override
    Response apply(Request request);

    record Request(List<UserPropertiesAttributes> searchAttributes) {
    }

    record Response(List<Property> properties) {
    }
}
