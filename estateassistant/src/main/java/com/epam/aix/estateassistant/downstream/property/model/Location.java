package com.epam.aix.estateassistant.downstream.property.model;

import lombok.Builder;

@Builder
public record Location(
        String address,
        String city,
        String neighborhood,
        String country
) {
}
