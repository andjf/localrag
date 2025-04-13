package dev.andrew.localrag.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("openai")
public record OpenAiProperties(String model, String apiKey) {
}
