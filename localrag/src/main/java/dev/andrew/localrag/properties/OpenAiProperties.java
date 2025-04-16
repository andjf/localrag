package dev.andrew.localrag.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("openai")
public record OpenAiProperties(
                String chatModel,
                String embeddingsModel,
                String apiKey) {
}
