package io.github.locxngo.chroma.embedding;

import io.github.locxngo.chroma.HttpStatus;
import io.github.locxngo.chroma.embedding.voyageai.TextEmbeddingModelRequest;
import io.github.locxngo.chroma.embedding.voyageai.TextEmbeddingModelResponse;
import io.github.locxngo.chroma.exception.CreateEmbeddingException;
import io.github.locxngo.chroma.schemas.EmbeddingFunctionConfig;
import io.github.locxngo.chroma.types.Embedding;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SuppressWarnings("java:S1192")
public class VoyageAIEmbeddingFunction extends BasicEmbeddingFunction {
    public static final String OPT_TRUNCATION = "truncation";
    private final boolean truncation;

    public static VoyageAIEmbeddingFunction fromConfig(Map<String, Object> config) {
        return new VoyageAIEmbeddingFunction(
            EmbeddingOption.builder()
                .apiKey(System.getenv("VOYAGEAI_API_KEY"))
                .model((String) config.getOrDefault("model", null))
                .options(config)
                .build()
        );
    }

    public VoyageAIEmbeddingFunction(EmbeddingOption option) {
        super(option);
        this.truncation = (boolean) option.getOptions().getOrDefault(OPT_TRUNCATION, true);
    }

    @Override
    protected String defaultBaseUrl() {
        return "https://api.voyageai.com/v1/embeddings";
    }

    @Override
    protected String defaultModel() {
        return "voyage-3.5";
    }

    @Override
    public EmbeddingModelRequest buildEmbeddingRequest(List<String> queryTexts) {
        return new TextEmbeddingModelRequest()
            .setInput(queryTexts)
            .setModel(getModel())
            .setTruncation(truncation);
    }

    @Override
    public CreateEmbeddingException buildExceptionFromResponse(Response response) {
        if (response.code() >= HttpStatus.SC_INTERNAL_SERVER_ERROR) {
            return new CreateEmbeddingException("Voyage service is getting use. Try again after a brief wait.");
        }
        return switch (response.code()) {
            case HttpStatus.SC_BAD_REQUEST -> new CreateEmbeddingException(
                """
                    The request is invalid, which might be due to one of the following reasons:
                    The request body is not a valid JSON;
                    A parameter is invalid or has the wrong type.
                    Batch size is too large;
                    Total number of tokens in the batch exceeds the limit;
                    Number of tokens in an example exceeds the context length;""");
            case HttpStatus.SC_UNAUTHORIZED -> new CreateEmbeddingException("Invalid authentication.");
            case HttpStatus.SC_FORBIDDEN -> new CreateEmbeddingException(
                "The IP address you are sending the request from might be forbidden.");
            case HttpStatus.SC_TOO_MANY_REQUESTS ->
                new CreateEmbeddingException("The frequency of your requests is too high.");
            default -> new CreateEmbeddingException(
                "Unknow exception " + response.code() + ", " + response.message());
        };
    }

    @Override
    public List<Embedding> buildListEmbeddings(Response response) {
        if (response.body() == null) {
            throw new CreateEmbeddingException("Response body from Voyage is blank");
        }
        try {
            TextEmbeddingModelResponse<Float> result = getObjectMapper().readValue(
                response.body().string(), TextEmbeddingModelResponse.FloatDtype.class
            );
            List<Embedding> results = new ArrayList<>();
            result.getData().forEach(o -> results.add(Embedding.from(o.getEmbedding())));
            return results;
        } catch (IOException exception) {
            throw new CreateEmbeddingException("Failed to deserialize response", exception);
        }
    }

    @Override
    public String name() {
        return "voyageai";
    }

    @Override
    public EmbeddingFunctionConfig toConfig() {
        return super.toConfig()
            .setConfig(Map.of(
                "model", getModel(),
                "truncation", truncation
            ));
    }
}
