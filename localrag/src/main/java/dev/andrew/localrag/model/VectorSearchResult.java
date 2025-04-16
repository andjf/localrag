package dev.andrew.localrag.model;

import java.io.IOException;
import java.util.Optional;

import org.apache.lucene.index.StoredFields;
import org.apache.lucene.search.ScoreDoc;

import lombok.Builder;

@Builder
public record VectorSearchResult(float score, String sentence) {

    public static VectorSearchResult from(ScoreDoc doc, StoredFields storedFields) {
        try {
            return VectorSearchResult.builder()
                    .score(doc.score)
                    .sentence(Optional.ofNullable(storedFields.document(doc.doc))
                            .map(d -> d.get(SentenceEmbedding.SENTENCE_FIELD_NAME))
                            .orElseThrow())
                    .build();
        } catch (IOException e) {
            return null;
        }
    }

}
