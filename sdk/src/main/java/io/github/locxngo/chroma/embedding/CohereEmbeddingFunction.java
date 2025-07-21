package io.github.locxngo.chroma.embedding;

import io.github.locxngo.chroma.HttpStatus;
import io.github.locxngo.chroma.embedding.cohere.InputType;
import io.github.locxngo.chroma.embedding.cohere.TextEmbeddingModelRequest;
import io.github.locxngo.chroma.embedding.cohere.TextEmbeddingModelResponse;
import io.github.locxngo.chroma.exception.CreateEmbeddingException;
import io.github.locxngo.chroma.types.Embedding;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CohereEmbeddingFunction extends BasicEmbeddingFunction {

    public static final int SC_INVALID_TOKEN = 498;
    public static final int SC_CLIENT_CLOSED_REQUEST = 499;

    public CohereEmbeddingFunction(EmbeddingOption option) {
        super(option);
    }

    public static CohereEmbeddingFunction fromConfig(Map<String, Object> config) {
        return new CohereEmbeddingFunction(
            EmbeddingOption.builder()
                .apiKey(System.getenv("COHERE_API_KEY"))
                .model((String) config.getOrDefault("model", null))
                .baseUrl((String) config.getOrDefault("baseUrl", null))
                .options(config)
                .build()
        );
    }

    @Override
    protected String defaultBaseUrl() {
        return "https://api.cohere.com/v2/embed";
    }

    @Override
    protected String defaultModel() {
        return "embed-v4.0";
    }

    @Override
    public EmbeddingModelRequest buildEmbeddingRequest(List<String> queryTexts) {
        return new TextEmbeddingModelRequest()
            .setModel(getModel())
            .setInputType(InputType.CLASSIFICATION)
            .setTexts(queryTexts)
            .setEmbeddingTypes(List.of(Dtype.FLOAT));
    }

    @Override
    public List<Embedding> buildListEmbeddings(Response response) {
        if (response.body() == null) {
            throw new CreateEmbeddingException("Response body from Cohere is blank");
        }
        try {
            TextEmbeddingModelResponse result = getObjectMapper().readValue(
                response.body().string(), TextEmbeddingModelResponse.class
            );
            List<Embedding> results = new ArrayList<>();
            result.getEmbeddings().getFloats()
                .forEach(floats -> results.add(Embedding.from(floats)));
            return results;
        } catch (IOException exception) {
            throw new CreateEmbeddingException("Failed to deserialize response", exception);
        }
    }

    @Override
    public CreateEmbeddingException buildExceptionFromResponse(Response response) {
        if (response.code() >= HttpStatus.SC_INTERNAL_SERVER_ERROR) {
            return new CreateEmbeddingException("Cohere service is getting use. Try again after a brief wait.");
        }
        return switch (response.code()) {
            case HttpStatus.SC_BAD_REQUEST, HttpStatus.SC_UNPROCESSABLE_CONTENT -> new CreateEmbeddingException(
                """
                    JSON is invalid, the request is missing required fields
                    or the request contains an invalid combination of fields""");
            case HttpStatus.SC_UNAUTHORIZED, HttpStatus.SC_FORBIDDEN -> new CreateEmbeddingException(
                "The api token is invalid or The user does not have the necessary permissions");
            case HttpStatus.SC_NOT_FOUND -> new CreateEmbeddingException(
                "The endpoint does not exist or The resource does not exist eg model id, dataset id");
            case SC_INVALID_TOKEN -> new CreateEmbeddingException(
                "This error is returned when a request or response contains a deny-listed token.");
            case SC_CLIENT_CLOSED_REQUEST -> new CreateEmbeddingException(
                "This error is returned when a request is cancelled by the user.");
            case HttpStatus.SC_TOO_MANY_REQUESTS ->
                new CreateEmbeddingException("The frequency of your requests is too high.");
            default -> new CreateEmbeddingException(
                "Unknow exception " + response.code() + ", " + response.message());
        };
    }

    @Override
    public String name() {
        return AIPlatform.COHERE.getValue();
    }
}
