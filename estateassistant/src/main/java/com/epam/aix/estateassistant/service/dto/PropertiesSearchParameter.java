package com.epam.aix.estateassistant.service.dto;

public record PropertiesSearchParameter(ParameterType name, boolean mandatory) {

    public static PropertiesSearchParameter of(ParameterType type, boolean mandatory) {
        return new PropertiesSearchParameter(type, mandatory);
    }
}
