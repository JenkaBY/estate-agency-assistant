package com.epam.aix.estateassistant.downstream.property.model;

import lombok.Builder;

import java.util.List;

@Builder
public record Property(
        String id,
        String title,
        String description,
        Location location,
        Price price,
        Integer sizeSqm,
        NumberOfRooms numberOfRooms,
        String propertyType,
        List<String> amenities,
        List<String> additionalFeatures,
        Integer yearBuilt,
        String listingDate,
        String cardLink
) {
}
