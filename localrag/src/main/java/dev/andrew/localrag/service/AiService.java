package dev.andrew.localrag.service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.google.common.collect.Streams;

import dev.andrew.localrag.model.SentenceEmbedding;
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
@Service
public class AiService {

    private static final double DEFAULT_TEMP = 0.5;
    private static final int DEFAULT_MAX_TOKENS = 300;

    private final OpenAiProperties openAiProperties;
    private final SimpleOpenAI openAI;

    public AiService(OpenAiProperties openAiProperties) {
        this.openAiProperties = openAiProperties;
        this.openAI = SimpleOpenAI.builder()
                .apiKey(this.openAiProperties.apiKey())
                .build();
    }

    public String chatModel() {
        return this.openAiProperties.chatModel();
    }

    public String embeddingModel() {
        return this.openAiProperties.embeddingsModel();
    }

    public ChatRequestBuilder chatRequestBuilder() {
        return ChatRequest.builder()
                .model(this.chatModel())
                .temperature(DEFAULT_TEMP)
                .maxCompletionTokens(DEFAULT_MAX_TOKENS);
    }

    public EmbeddingRequestBuilder embeddingRequestBuilder() {
        return EmbeddingRequest.builder()
                .model(this.embeddingModel())
                .encodingFormat(EncodingFormat.FLOAT);
    }

    public String chat(ChatRequest chat) {
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

    public Stream<String> chatStream(ChatRequest chat) {
        var futureChat = openAI.chatCompletions().createStream(chat);
        var chatResponse = futureChat.join();
        return chatResponse.map(this::extractToken).filter(Objects::nonNull);
    }

    public List<List<Double>> embeddings(EmbeddingRequest embeddingRequest) {
        List<EmbeddingFloat> embeddingData = this.openAI.embeddings()
                .create(embeddingRequest)
                .join()
                .getData();

        return embeddingData.stream()
                .map(EmbeddingFloat::getEmbedding)
                .toList();
    }

    public List<SentenceEmbedding> sentenceEmbeddings(List<String> sentences) {
        EmbeddingRequest req = this.embeddingRequestBuilder()
                .input(sentences)
                .dimensions(1024)
                .build();

        List<List<Double>> embeddings = this.embeddings(req);

        Assert.isTrue(sentences.size() == embeddings.size(), "Embeddings were not generated for every sentence");

        return Streams.zip(sentences.stream(), embeddings.stream(), (s, e) -> new SentenceEmbedding(s, e)).toList();
    }

    public SentenceEmbedding embedding(String text) {
        List<SentenceEmbedding> embeddings = this.sentenceEmbeddings(List.of(text));
        Assert.isTrue(embeddings.size() == 1, "Expected 1 embedding. Got " + embeddings.size());
        return embeddings.get(0);
    }

}
