package dev.andrew.localrag.controller;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;

import dev.andrew.localrag.properties.OpenAiProperties;
import io.github.sashirestela.openai.SimpleOpenAI;
import io.github.sashirestela.openai.domain.chat.Chat;
import io.github.sashirestela.openai.domain.chat.Chat.Choice;
import io.github.sashirestela.openai.domain.chat.ChatMessage.ResponseMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.domain.chat.ChatRequest.ChatRequestBuilder;
import io.github.sashirestela.openai.domain.embedding.EmbeddingFloat;
import io.github.sashirestela.openai.domain.embedding.EmbeddingRequest;
import io.github.sashirestela.openai.domain.embedding.EmbeddingRequest.EmbeddingRequestBuilder;
import io.github.sashirestela.openai.domain.embedding.EmbeddingRequest.EncodingFormat;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public abstract class BaseAiController {

    private static final double DEFAULT_TEMP = 0.5;
    private static final int DEFAULT_MAX_TOKENS = 300;

    protected final OpenAiProperties openAiProperties;
    protected final SimpleOpenAI openAI;

    public BaseAiController(OpenAiProperties openAiProperties) {
        this.openAiProperties = openAiProperties;
        this.openAI = SimpleOpenAI.builder()
                .apiKey(this.openAiProperties.apiKey())
                .build();
    }

    protected String chatModel() {
        return this.openAiProperties.chatModel();
    }

    protected String embeddingModel() {
        return this.openAiProperties.embeddingsModel();
    }

    protected ChatRequestBuilder chatRequestBuilder() {
        return ChatRequest.builder()
                .model(this.chatModel())
                .temperature(DEFAULT_TEMP)
                .maxCompletionTokens(DEFAULT_MAX_TOKENS);
    }

    protected EmbeddingRequestBuilder embeddingRequestBuilder() {
        return EmbeddingRequest.builder()
                .model(this.embeddingModel())
                .encodingFormat(EncodingFormat.FLOAT);
    }

    protected String chat(ChatRequest chat) {
        return openAI.chatCompletions()
                .create(chat)
                .join()
                .firstContent();
    }

    private boolean shouldChoose(Choice choice) {
        return Optional.ofNullable(choice)
                .map(Choice::getMessage)
                .map(ResponseMessage::getContent)
                .filter(StringUtils::isNotEmpty)
                .isPresent();
    }

    private String extractToken(Chat chat) {
        return Optional.ofNullable(chat)
                .map(Chat::getChoices)
                .map(choices -> choices.stream()
                        .filter(this::shouldChoose)
                        .findFirst()
                        .orElse(null))
                .map(Choice::getMessage)
                .map(ResponseMessage::getContent)
                .orElse(null);
    }

    protected Stream<String> chatStream(ChatRequest chat) {
        var futureChat = openAI.chatCompletions().createStream(chat);
        var chatResponse = futureChat.join();
        return chatResponse.map(this::extractToken).filter(Objects::nonNull);
    }

    protected List<List<Double>> embeddings(EmbeddingRequest embeddingRequest) {
        List<EmbeddingFloat> embeddingData = this.openAI.embeddings()
                .create(embeddingRequest)
                .join()
                .getData();

        return embeddingData.stream()
                .map(EmbeddingFloat::getEmbedding)
                .toList();
    }

}
