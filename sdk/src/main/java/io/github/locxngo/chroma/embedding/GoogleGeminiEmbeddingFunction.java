package io.github.locxngo.chroma.embedding;

import io.github.locxngo.chroma.Constants;
import io.github.locxngo.chroma.embedding.google.gemini.GeminiEmbeddingRequest;
import io.github.locxngo.chroma.embedding.google.gemini.GeminiEmbeddingResponse;
import io.github.locxngo.chroma.exception.CreateEmbeddingException;
import io.github.locxngo.chroma.types.Embedding;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@SuppressWarnings("java:S1192")
public class GoogleGeminiEmbeddingFunction extends BasicEmbeddingFunction {

    public static GoogleGeminiEmbeddingFunction fromConfig(Map<String, Object> config) {
        return new GoogleGeminiEmbeddingFunction(
                EmbeddingOption.builder()
                        .apiKey(System.getenv("GOOGLE_GEMINI_API_KEY"))
                        .model((String) config.getOrDefault("model", null))
                        .options(config)
                        .build());
    }

    public GoogleGeminiEmbeddingFunction(EmbeddingOption option) {
        super(option);
    }

    @Override
    protected String defaultBaseUrl() {
        return "https://generativelanguage.googleapis.com/v1beta";
    }

    @Override
    protected String defaultModel() {
        return "models/gemini-embedding-001";
    }

    @Override
    public EmbeddingModelRequest buildEmbeddingRequest(List<String> queryTexts) {
        if (queryTexts == null || queryTexts.isEmpty()) {
            return null;
        }
        return new GeminiEmbeddingRequest()
                .setModel(getModel())
                .setContent(new GeminiEmbeddingRequest.Content()
                        .setParts(Collections.singletonList(
                                new GeminiEmbeddingRequest.Part().setText(queryTexts.get(0)))));
    }

    @Override
    public List<Embedding> buildListEmbeddings(Response response) {
        try {
            if (response.body() == null) {
                throw new CreateEmbeddingException("Response body is null");
            }
            GeminiEmbeddingResponse result = getObjectMapper().readValue(
                    response.body().string(), GeminiEmbeddingResponse.class);
            if (result.getEmbedding() != null && result.getEmbedding().getValues() != null) {
                return Collections.singletonList(Embedding.from(result.getEmbedding().getValues()));
            }
            return Collections.emptyList();
        } catch (IOException e) {
            throw new CreateEmbeddingException("Failed to deserialize response", e);
        }
    }

    @Override
    public List<Embedding> embedText(List<String> queryTexts) throws CreateEmbeddingException {
        List<Embedding> embeddings = new ArrayList<>();
        for (String text : queryTexts) {
            embeddings.addAll(embedSingleText(text));
        }
        return embeddings;
    }

    private List<Embedding> embedSingleText(String text) {
        GeminiEmbeddingRequest payload = (GeminiEmbeddingRequest) buildEmbeddingRequest(
                Collections.singletonList(text));
        String jsonPayload;
        try {
            jsonPayload = getObjectMapper().writeValueAsString(payload);
        } catch (IOException exception) {
            throw new CreateEmbeddingException("Failed to create json payload", exception);
        }

        String fullUrl = getBaseUrl();
        if (!fullUrl.contains(":embedContent")) {
            if (fullUrl.endsWith("/")) {
                fullUrl += getModel() + ":embedContent";
            } else {
                fullUrl += "/" + getModel() + ":embedContent";
            }
        }

        Request request = new Request.Builder()
                .url(fullUrl)
                .post(RequestBody.create(jsonPayload, Constants.JSON))
                .addHeader("Content-Type", "application/json")
                .addHeader("x-goog-api-key", getApiKey())
                .build();

        try (Response response = getClient().newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw buildExceptionFromResponse(response);
            }
            return buildListEmbeddings(response);
        } catch (IOException e) {
            throw new CreateEmbeddingException("Failed to request to Gemini", e);
        }
    }

    @Override
    public String name() {
        return AIPlatform.GOOGLE_GEMINI.getValue();
    }
}
