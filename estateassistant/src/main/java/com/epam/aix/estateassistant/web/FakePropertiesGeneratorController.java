package com.epam.aix.estateassistant.web;

import com.epam.aix.estateassistant.service.PropertiesService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/generators/properties")
@RestController
public class FakePropertiesGeneratorController {

    private final PropertiesService propertiesService;

    @PostMapping
    public ResponseEntity<?> fakePropertiesGenerator(@RequestBody LocationRequest request) {
        log.info("Received request for properties in location: {}", request.location());
        return ResponseEntity.ok(
                propertiesService.getPropertiesForLocation(request.location())
        );
    }
}
