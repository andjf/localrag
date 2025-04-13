package dev.andrew.localrag.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import opennlp.tools.namefind.NameFinderME;
import opennlp.tools.namefind.TokenNameFinderModel;
import opennlp.tools.sentdetect.SentenceDetectorME;
import opennlp.tools.sentdetect.SentenceModel;
import opennlp.tools.tokenize.TokenizerME;
import opennlp.tools.tokenize.TokenizerModel;
import opennlp.tools.util.Span;

@Service
public class NlpService {

    private final SentenceModel sentenceModel;
    private final TokenizerModel tokenizerModel;
    private final TokenNameFinderModel dateFinderModel;
    private final TokenNameFinderModel locationFinderModel;
    private final TokenNameFinderModel moneyFinderModel;
    private final TokenNameFinderModel organizationFinderModel;
    private final TokenNameFinderModel percentageFinderModel;
    private final TokenNameFinderModel personFinderModel;
    private final TokenNameFinderModel timeFinderModel;

    public NlpService(
            SentenceModel sentenceModel,
            TokenizerModel tokenizerModel,
            @Qualifier("nerDateModel") TokenNameFinderModel dateFinderModel,
            @Qualifier("nerLocationModel") TokenNameFinderModel locationFinderModel,
            @Qualifier("nerMoneyModel") TokenNameFinderModel moneyFinderModel,
            @Qualifier("nerOrganizationModel") TokenNameFinderModel organizationFinderModel,
            @Qualifier("nerPercentageModel") TokenNameFinderModel percentageFinderModel,
            @Qualifier("nerPersonModel") TokenNameFinderModel personFinderModel,
            @Qualifier("nerTimeModel") TokenNameFinderModel timeFinderModel) {
        this.sentenceModel = sentenceModel;
        this.tokenizerModel = tokenizerModel;
        this.dateFinderModel = dateFinderModel;
        this.locationFinderModel = locationFinderModel;
        this.moneyFinderModel = moneyFinderModel;
        this.organizationFinderModel = organizationFinderModel;
        this.percentageFinderModel = percentageFinderModel;
        this.personFinderModel = personFinderModel;
        this.timeFinderModel = timeFinderModel;
    }

    public String[] sentences(String body) {
        return new SentenceDetectorME(this.sentenceModel).sentDetect(body);
    }

    public String[] tokenize(String body) {
        return new TokenizerME(this.tokenizerModel).tokenize(body);
    }

    public String[] dates(String body) {
        return this.dates(this.tokenize(body));
    }

    public String[] dates(String[] tokens) {
        Span[] spans = new NameFinderME(this.dateFinderModel).find(tokens);
        return Span.spansToStrings(spans, tokens);
    }

    public String[] locations(String body) {
        return this.locations(this.tokenize(body));
    }

    public String[] locations(String[] tokens) {
        Span[] spans = new NameFinderME(this.locationFinderModel).find(tokens);
        return Span.spansToStrings(spans, tokens);
    }

    public String[] money(String body) {
        return this.money(this.tokenize(body));
    }

    public String[] money(String[] tokens) {
        Span[] spans = new NameFinderME(this.moneyFinderModel).find(tokens);
        return Span.spansToStrings(spans, tokens);
    }

    public String[] organizations(String body) {
        return this.organizations(this.tokenize(body));
    }

    public String[] organizations(String[] tokens) {
        Span[] spans = new NameFinderME(this.organizationFinderModel).find(tokens);
        return Span.spansToStrings(spans, tokens);
    }

    public String[] percentages(String body) {
        return this.percentages(this.tokenize(body));
    }

    public String[] percentages(String[] tokens) {
        Span[] spans = new NameFinderME(this.percentageFinderModel).find(tokens);
        return Span.spansToStrings(spans, tokens);
    }

    public String[] persons(String body) {
        return this.persons(this.tokenize(body));
    }

    public String[] persons(String[] tokens) {
        Span[] spans = new NameFinderME(this.personFinderModel).find(tokens);
        return Span.spansToStrings(spans, tokens);
    }

    public String[] times(String body) {
        return this.times(this.tokenize(body));
    }

    public String[] times(String[] tokens) {
        Span[] spans = new NameFinderME(this.timeFinderModel).find(tokens);
        return Span.spansToStrings(spans, tokens);
    }

}
