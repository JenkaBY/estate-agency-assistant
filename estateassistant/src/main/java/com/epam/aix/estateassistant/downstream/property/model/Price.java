package com.epam.aix.estateassistant.downstream.property.model;

import lombok.Builder;

@Builder
public record Price(
        Integer amount,
        String currency
) {
}
