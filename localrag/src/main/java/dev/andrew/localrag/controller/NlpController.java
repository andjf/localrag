package dev.andrew.localrag.controller;

import java.io.IOException;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.andrew.localrag.service.NlpService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/nlp")
public class NlpController {

    private final NlpService nlpService;

    @PostMapping(value = "/sentence", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.TEXT_PLAIN_VALUE)
    public String[] sentences(@RequestBody String body) throws IOException {
        String[] sentences = this.nlpService.sentences(body);
        log.info("Split body of size [{}] into [{}] sentences", body.length(), sentences.length);
        return sentences;
    }

    @PostMapping(value = "/entities", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.TEXT_PLAIN_VALUE)
    public Map<String, String[]> entities(@RequestBody String body) throws IOException {
        String[] tokens = this.nlpService.tokenize(body);
        Map<String, Function<String[], String[]>> entities = Map.of(
                "dates", this.nlpService::dates,
                "money", this.nlpService::money,
                "organizations", this.nlpService::organizations,
                "percentages", this.nlpService::percentages,
                "persons", this.nlpService::persons,
                "times", this.nlpService::times);
        return entities.entrySet().stream()
                .unordered()
                .parallel()
                .collect(Collectors.toMap(Map.Entry::getKey, entry -> entry.getValue().apply(tokens)));
    }

}
