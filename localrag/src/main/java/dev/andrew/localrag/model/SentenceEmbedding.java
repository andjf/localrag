package dev.andrew.localrag.model;

import java.util.List;

import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.KnnFloatVectorField;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.VectorSimilarityFunction;
import org.apache.lucene.search.KnnFloatVectorQuery;

public record SentenceEmbedding(String sentence, List<Double> embedding) {

    private final static VectorSimilarityFunction SIMILARITY_FUNCTION = VectorSimilarityFunction.COSINE;

    public final static String SENTENCE_FIELD_NAME = "sentence";
    public final static String EMBEDDING_FIELD_NAME = "embedding";

    public float[] embeddingAsFloatArray() {
        float[] floatValues = new float[embedding.size()];
        for (int i = 0; i < embedding.size(); i++) {
            floatValues[i] = embedding.get(i).floatValue();
        }
        return floatValues;
    }

    public Document asLuceneDocument() {
        Document doc = new Document();
        doc.add(new TextField(
                SentenceEmbedding.SENTENCE_FIELD_NAME,
                this.sentence(),
                Field.Store.YES));
        doc.add(new KnnFloatVectorField(
                SentenceEmbedding.EMBEDDING_FIELD_NAME,
                this.embeddingAsFloatArray(),
                SentenceEmbedding.SIMILARITY_FUNCTION));
        return doc;
    }

    public KnnFloatVectorQuery asLuceneVectorQuery(int k) {
        return new KnnFloatVectorQuery(
                SentenceEmbedding.EMBEDDING_FIELD_NAME,
                this.embeddingAsFloatArray(),
                k);
    }

}
