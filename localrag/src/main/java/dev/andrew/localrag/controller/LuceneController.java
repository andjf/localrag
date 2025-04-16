package dev.andrew.localrag.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import dev.andrew.localrag.model.VectorSearchResult;
import dev.andrew.localrag.service.LuceneService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/lucene")
@RequiredArgsConstructor
public class LuceneController {

    private final LuceneService luceneService;

    @PostMapping("/index")
    public List<String> index(@RequestBody String text) throws IOException {
        return this.luceneService.index(text);
    }

    @GetMapping("/knn")
    public List<VectorSearchResult> knn(
            @RequestParam(required = true) String q,
            @RequestParam(required = false, defaultValue = "10") int k) throws IOException {
        return this.luceneService.knn(q, k);
    }

}
