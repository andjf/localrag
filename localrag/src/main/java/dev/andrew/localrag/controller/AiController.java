package dev.andrew.localrag.controller;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.common.collect.Streams;

import dev.andrew.localrag.properties.OpenAiProperties;
import dev.andrew.localrag.service.NlpService;
import io.github.sashirestela.openai.domain.chat.ChatMessage.SystemMessage;
import io.github.sashirestela.openai.domain.chat.ChatMessage.UserMessage;
import io.github.sashirestela.openai.domain.chat.ChatRequest;
import io.github.sashirestela.openai.domain.embedding.EmbeddingRequest;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/v1")
public class AiController extends BaseAiController {

    NlpService nlpService;

    public AiController(OpenAiProperties openAiProperties, NlpService nlpService) {
        super(openAiProperties);
        this.nlpService = nlpService;
    }

    @PostMapping(value = "/chat", produces = MediaType.TEXT_PLAIN_VALUE, consumes = MediaType.TEXT_PLAIN_VALUE)
    public String chat(@RequestBody String prompt) throws IOException {
        log.info("Initiated /v1/chat request with prmopt [{}]", prompt);

        ChatRequest chat = chatRequestBuilder()
                .message(SystemMessage.of("You are an expert in AI designd to answer any question."))
                .message(UserMessage.of(prompt))
                .build();

        return super.chat(chat);
    }

    private record Embedding(String sentence, List<Double> embedding) {
    }

    @PostMapping(value = "/embeddings", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.TEXT_PLAIN_VALUE)
    public List<Embedding> embedding(@RequestBody String text) throws IOException {
        log.info("Initiated /v1/embeddings request with text [{}]", text);

        List<String> sentences = Arrays.asList(this.nlpService.sentences(text));

        EmbeddingRequest req = embeddingRequestBuilder()
                .input(sentences)
                .build();

        List<List<Double>> embeddings = super.embeddings(req);

        Assert.isTrue(sentences.size() == embeddings.size(), "Embeddings were not generated for every sentence");

        return Streams.zip(sentences.stream(), embeddings.stream(), (s, e) -> new Embedding(s, e))
                .toList();
    }

}
