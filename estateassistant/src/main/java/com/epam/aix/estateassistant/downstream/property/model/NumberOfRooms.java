package com.epam.aix.estateassistant.downstream.property.model;

import lombok.Builder;

@Builder
public record NumberOfRooms(
        Integer bedrooms,
        Integer bathrooms
) {
}
