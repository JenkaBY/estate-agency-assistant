package com.epam.aix.estateassistant.service.dto;

public enum ParameterType {
    SERVICE_TYPE("rent, buy"),
    LOCATION("country, city, neighborhood"),
    PRICE("exact price"),
    SIZE ("square meters"),
    NUMBER_OF_ROOMS("number of bedrooms and bathrooms"),
    PROPERTY_TYPE ("apartment, house, condo and so on"),
    AMENITIES("pool, gym, parking and so on"),
    ADDITIONAL_FEATURES("proximity to schools pre-school, public transportation and so on");
    private final String description;

    ParameterType(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return name()
                + "{" +
                "description='" + description + '\'' +
                '}';
    }
}
