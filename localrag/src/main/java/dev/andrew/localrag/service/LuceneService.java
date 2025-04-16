package dev.andrew.localrag.service;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.apache.lucene.document.Document;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.StoredFields;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.Directory;
import org.springframework.stereotype.Service;

import dev.andrew.localrag.model.SentenceEmbedding;
import dev.andrew.localrag.model.VectorSearchResult;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LuceneService {

    private final AiService aiService;
    private final NlpService nlpService;
    private final Directory inMemoryIndexDirectory;
    private final IndexWriterConfig config;

    public List<String> index(String text) throws IOException {
        try (IndexWriter writer = new IndexWriter(this.inMemoryIndexDirectory, this.config)) {
            List<String> sentences = Arrays.asList(this.nlpService.sentences(text));
            List<SentenceEmbedding> embeddings = this.aiService.sentenceEmbeddings(sentences);

            List<Document> documents = embeddings.stream()
                    .map(SentenceEmbedding::asLuceneDocument)
                    .toList();

            for (var doc : documents) {
                writer.addDocument(doc);
            }

            return sentences;
        }
    }

    public List<VectorSearchResult> knn(String q, int k) throws IOException {
        try (IndexReader reader = DirectoryReader.open(this.inMemoryIndexDirectory)) {
            IndexSearcher searcher = new IndexSearcher(reader);

            SentenceEmbedding queryEmbedding = this.aiService.embedding(q);
            TopDocs topDocs = searcher.search(queryEmbedding.asLuceneVectorQuery(k), k);

            StoredFields storedFields = searcher.storedFields();

            return Arrays.stream(topDocs.scoreDocs)
                    .map(doc -> VectorSearchResult.from(doc, storedFields))
                    .filter(Objects::nonNull)
                    .toList();
        }
    }
}
