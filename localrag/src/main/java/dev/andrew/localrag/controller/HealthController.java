package dev.andrew.localrag.controller;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
public class HealthController {

    @Value("${spring.application.name}")
    String applicationName;

    @GetMapping(value = { "/", "/health" }, produces = MediaType.APPLICATION_JSON_VALUE)
    public Map<String, String> health() {
        return Map.of(
                "name", applicationName,
                "status", "OK");
    }

}
