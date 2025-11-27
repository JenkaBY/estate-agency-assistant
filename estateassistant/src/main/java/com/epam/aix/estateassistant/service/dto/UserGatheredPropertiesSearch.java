package com.epam.aix.estateassistant.service.dto;

import lombok.Builder;

import java.util.List;

@Builder
public record UserGatheredPropertiesSearch(boolean isAllDataCollected,
                                           String textResponse,
                                           List<UserPropertiesAttributes> searchAttributes) {
}
