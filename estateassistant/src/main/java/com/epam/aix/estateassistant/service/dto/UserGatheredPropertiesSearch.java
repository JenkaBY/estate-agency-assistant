package com.epam.aix.estateassistant.service.dto;

import java.util.List;

public record UserGatheredPropertiesSearch(boolean isAllDataCollected,
                                           String textResponse,
                                           List<UserPropertiesAttributes> searchAttributes) {
}
