package io.github.locxngo.chroma.embedding;

import io.github.locxngo.chroma.HttpStatus;
import io.github.locxngo.chroma.embedding.openai.TextEmbeddingModelRequest;
import io.github.locxngo.chroma.embedding.openai.TextEmbeddingModelResponse;
import io.github.locxngo.chroma.exception.CreateEmbeddingException;
import io.github.locxngo.chroma.types.Embedding;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class OpenAIEmbeddingFunction extends BasicEmbeddingFunction {
    public static final String OPT_USER = "user";
    public static final String OPT_DIMENSIONS = "dimensions";
    public static final String OPT_FORMAT = "encoding_format";
    private final String user;
    private final String encodingFormat;
    private final int dimensions;

    public static OpenAIEmbeddingFunction fromConfig(Map<String, Object> config) {
        return new OpenAIEmbeddingFunction(
            EmbeddingOption.builder()
                .apiKey(System.getenv("OPENAI_API_KEY"))
                .model((String) config.getOrDefault("model", null))
                .options(config)
                .build()
        );
    }

    public OpenAIEmbeddingFunction(EmbeddingOption option) {
        super(option);
        user = (String) option.getOptions().getOrDefault(OPT_USER, null);
        dimensions = (int) option.getOptions().getOrDefault(OPT_DIMENSIONS, 0);
        encodingFormat = (String) option.getOptions().getOrDefault(OPT_FORMAT, null);
    }

    @Override
    protected String defaultBaseUrl() {
        return "https://api.openai.com/v1/embeddings";
    }

    @Override
    protected String defaultModel() {
        return "text-embedding-3-small";
    }

    @Override
    public EmbeddingModelRequest buildEmbeddingRequest(List<String> queryTexts) {
        return new TextEmbeddingModelRequest()
            .setDimensions(dimensions)
            .setUser(user)
            .setEncodingFormat(encodingFormat)
            .setModel(getModel())
            .setInput(queryTexts);
    }

    @Override
    public CreateEmbeddingException buildExceptionFromResponse(Response response) {
        if (response.body() == null) {
            return super.buildExceptionFromResponse(response);
        }
        return switch (response.code()) {
            case HttpStatus.SC_UNAUTHORIZED -> new CreateEmbeddingException(
                "Invalid Authentication: Ensure the correct API key and requesting organization are being used."
            );
            case HttpStatus.SC_FORBIDDEN -> new CreateEmbeddingException(
                "You are accessing the API from an unsupported country, region, or territory."
            );
            case HttpStatus.SC_TOO_MANY_REQUESTS -> new CreateEmbeddingException(
                "You are sending requests too quickly."
            );
            case HttpStatus.SC_INTERNAL_SERVER_ERROR -> new CreateEmbeddingException("Issue on OpenAI servers.");
            case HttpStatus.SC_SERVICE_UNAVAILABLE -> new CreateEmbeddingException(
                "OpenAI servers are experiencing high traffic. Please retry your requests after a brief wait."
            );
            default -> new CreateEmbeddingException(
                "Unknow exception " + response.code() + ", " + response.message());
        };
    }

    @Override
    public List<Embedding> buildListEmbeddings(Response response) {
        if (response.body() == null) {
            throw new CreateEmbeddingException("Response body from OpenAI is blank");
        }
        try {
            TextEmbeddingModelResponse result = getObjectMapper().readValue(
                response.body().string(), TextEmbeddingModelResponse.class
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
        return AIPlatform.OPENAI.getValue();
    }
}
