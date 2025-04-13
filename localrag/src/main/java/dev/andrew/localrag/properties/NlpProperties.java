package dev.andrew.localrag.properties;

import java.io.InputStream;

import org.apache.commons.lang3.Validate;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("nlp")
public record NlpProperties(
        String sentenceModel,
        String tokenizationModel,
        String nerDateModel,
        String nerLocationModel,
        String nerMoneyModel,
        String nerOrganizationModel,
        String nerPercentageModel,
        String nerPersonModel,
        String nerTimeModel) {

    public static InputStream asInputStream(String model) {
        return Validate.notNull(
                NlpProperties.class.getClassLoader().getResourceAsStream(model),
                "Pre-trained model \"%s\" not found",
                model);
    }

}
