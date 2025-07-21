package io.github.locxngo.chroma.embedding;

import io.github.locxngo.chroma.Constants;
import io.github.locxngo.chroma.exception.CreateEmbeddingException;
import io.github.locxngo.chroma.exception.InvalidEmbeddingOptionException;
import io.github.locxngo.chroma.schemas.EmbeddingFunctionConfig;
import io.github.locxngo.chroma.types.Embedding;
import io.github.locxngo.chroma.types.Space;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.List;
import java.util.Map;

@Setter
@Getter
@Accessors(chain = true)
public abstract class BasicEmbeddingFunction implements EmbeddingFunction {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OkHttpClient client = new OkHttpClient();
    private String baseUrl;
    private String apiKey;
    private String model;

    BasicEmbeddingFunction(EmbeddingOption option) {
        this.baseUrl = option.getBaseUrl();
        if (StringUtils.isBlank(baseUrl)) {
            this.baseUrl = defaultBaseUrl();
        }
        this.model = option.getModel();
        if (StringUtils.isBlank(model)) {
            this.model = defaultModel();
        }
        this.apiKey = option.getApiKey();
        if (!isLocal() && StringUtils.isAnyBlank(baseUrl, model, apiKey)) {
            throw new InvalidEmbeddingOptionException("Base URL, model, and api key must not blank");
        }
    }


    /**
     * returns {@code true} if this function run locally, otherwise returns {@code false}
     */
    protected boolean isLocal() {
        return false;
    }

    protected abstract String defaultBaseUrl();

    protected abstract String defaultModel();

    public abstract EmbeddingModelRequest buildEmbeddingRequest(List<String> queryTexts);

    public abstract List<Embedding> buildListEmbeddings(Response response);

    public CreateEmbeddingException buildExceptionFromResponse(Response response) {
        return new CreateEmbeddingException(
            "Failed to execute embedding request. Code = " + response.code() + ", message = " + response.message());
    }

    @Override
    public List<Embedding> embedText(List<String> queryTexts) throws CreateEmbeddingException {
        EmbeddingModelRequest payload = buildEmbeddingRequest(queryTexts);
        String jsonPayload = null;
        try {
            jsonPayload = getObjectMapper().writeValueAsString(payload);
        } catch (IOException exception) {
            throw new CreateEmbeddingException("Failed to create json payload", exception);
        }
        Request request = new Request.Builder()
            .url(getBaseUrl())
            .post(RequestBody.create(jsonPayload, Constants.JSON))
            .addHeader("Accept", "application/json")
            .addHeader("Content-Type", "application/json")
            .addHeader("Authorization", "Bearer " + getApiKey())
            .build();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw buildExceptionFromResponse(response);
            }
            return buildListEmbeddings(response);
        } catch (IOException e) {
            throw new CreateEmbeddingException("Failed to request to Voyage platform", e);
        }
    }

    @Override
    public String defaultSpace() {
        return Space.COSINE.getValue();
    }

    @Override
    public List<String> supportedSpaces() {
        return List.of(
            Space.COSINE.getValue(),
            Space.L2.getValue(),
            Space.IP.getValue()
        );
    }

    @Override
    public EmbeddingFunctionConfig toConfig() {
        return new EmbeddingFunctionConfig()
            .setType("known")
            .setName(name())
            .setConfig(Map.of(
                "model", getModel()
            ));
    }
}
