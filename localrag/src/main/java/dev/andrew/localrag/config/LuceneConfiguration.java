package dev.andrew.localrag.config;

import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.store.Directory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LuceneConfiguration {

    @Bean
    Directory heapIndex() {
        return new ByteBuffersDirectory();
    }

    @Bean
    IndexWriterConfig indexWriterConfig() {
        return new IndexWriterConfig();
    }

}
