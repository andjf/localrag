package dev.andrew.localrag.config;

import java.io.IOException;
import java.io.InputStream;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import dev.andrew.localrag.properties.NlpProperties;
import lombok.extern.slf4j.Slf4j;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerModel;

@Slf4j
@Configuration
public class NlpConfiguration {

    @Bean
    SentenceModel sentenceModel(NlpProperties nlpProperties) {
        try (InputStream modelInput = NlpProperties.asInputStream(nlpProperties.sentenceModel())) {
            return new SentenceModel(modelInput);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create SentenceModel bean", e);
        }
    }

    @Bean
    TokenizerModel tokenizerModel(NlpProperties nlpProperties) {
        try (InputStream modelInput = NlpProperties.asInputStream(nlpProperties.tokenizationModel())) {
            return new TokenizerModel(modelInput);
        } catch (IOException e) {
            throw new IllegalStateException("Failed to create SentenceModel bean", e);
        }
    }

    @Bean
    TokenNameFinderModel nerDateModel(NlpProperties nlpProperties) {
        try(InputStream modelInput = NlpProperties.asInputStream(nlpProperties.nerDateModel())) {
            return new TokenNameFinderModel(modelInput);
        } catch(IOException e) {
            throw new IllegalStateException("Failed to create TokenNameFinderModel for [date]");
        }
    }

    @Bean
    TokenNameFinderModel nerLocationModel(NlpProperties nlpProperties) {
        try(InputStream modelInput = NlpProperties.asInputStream(nlpProperties.nerLocationModel())) {
            return new TokenNameFinderModel(modelInput);
        } catch(IOException e) {
            throw new IllegalStateException("Failed to create TokenNameFinderModel for [location]");
        }
    }

    @Bean
    TokenNameFinderModel nerMoneyModel(NlpProperties nlpProperties) {
        try(InputStream modelInput = NlpProperties.asInputStream(nlpProperties.nerMoneyModel())) {
            return new TokenNameFinderModel(modelInput);
        } catch(IOException e) {
            throw new IllegalStateException("Failed to create TokenNameFinderModel for [money]");
        }
    }

    @Bean
    TokenNameFinderModel nerOrganizationModel(NlpProperties nlpProperties) {
        try(InputStream modelInput = NlpProperties.asInputStream(nlpProperties.nerOrganizationModel())) {
            return new TokenNameFinderModel(modelInput);
        } catch(IOException e) {
            throw new IllegalStateException("Failed to create TokenNameFinderModel for [organization]");
        }
    }

    @Bean
    TokenNameFinderModel nerPercentageModel(NlpProperties nlpProperties) {
        try(InputStream modelInput = NlpProperties.asInputStream(nlpProperties.nerPercentageModel())) {
            return new TokenNameFinderModel(modelInput);
        } catch(IOException e) {
            throw new IllegalStateException("Failed to create TokenNameFinderModel for [percentage]");
        }
    }

    @Bean
    TokenNameFinderModel nerPersonModel(NlpProperties nlpProperties) {
        try(InputStream modelInput = NlpProperties.asInputStream(nlpProperties.nerPersonModel())) {
            return new TokenNameFinderModel(modelInput);
        } catch(IOException e) {
            throw new IllegalStateException("Failed to create TokenNameFinderModel for [person]");
        }
    }

    @Bean
    TokenNameFinderModel nerTimeModel(NlpProperties nlpProperties) {
        try(InputStream modelInput = NlpProperties.asInputStream(nlpProperties.nerTimeModel())) {
            return new TokenNameFinderModel(modelInput);
        } catch(IOException e) {
            throw new IllegalStateException("Failed to create TokenNameFinderModel for [time]");
        }
    }

}
