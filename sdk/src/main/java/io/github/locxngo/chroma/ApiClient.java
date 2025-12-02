package io.github.locxngo.chroma;

import io.github.locxngo.chroma.exception.ChromaAPIException;
import io.github.locxngo.chroma.exception.InvalidConfigException;
import io.github.locxngo.chroma.exception.ResponseInitializeException;
import io.github.locxngo.chroma.schemas.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;

@Setter
@Getter
@Accessors(chain = true)
public class ApiClient {

    private final ObjectMapper objectMapper = new ObjectMapper();
    private final OkHttpClient client = new OkHttpClient();
    private String baseUrl;
    private Headers headers;

    public ApiClient(String baseUrl, Map<String, String> headers) {
        if (StringUtils.isBlank(baseUrl)) {
            throw new InvalidConfigException("baseUrl must not blank");
        }
        if (baseUrl.endsWith("/")) {
            baseUrl = StringUtils.chop(baseUrl);
        }
        this.baseUrl = baseUrl;
        this.headers = Headers.of(headers == null ? Map.of() : headers);
    }

    public Response get(String path) throws IOException {
        Request request = new Request.Builder()
            .url(baseUrl + path).get()
            .headers(headers)
            .build();
        return client.newCall(request).execute();
    }

    public <R extends ErrorResponse> R get(String path, Class<R> clazz) {
        try (Response response = get(path)) {
            if (!response.isSuccessful()) {
                if (response.body() == null) {
                    throw new ChromaAPIException("Response data is null");
                }
                R result = getObjectMapper().readValue(response.body().string(), clazz);
                result.setErrorCode(response.code());
                return result;
            }
            if (response.body() == null) {
                throw new ChromaAPIException("Response data is null");
            }
            return getObjectMapper().readValue(response.body().string(), clazz);
        } catch (IOException exception) {
            try {
                R result = clazz.getConstructor().newInstance();
                result.setErrorCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .setError(Constants.ERROR_IO)
                    .setMessage(exception.getMessage());
                return result;
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException
                     | IllegalAccessException e) {
                throw new ResponseInitializeException("Failed to init class " + clazz.getName());
            }
        }
    }

    public Response post(String path, RequestBody body) throws IOException {
        Request request = new Request.Builder()
            .url(baseUrl + path).post(body)
            .headers(headers)
            .build();
        return client.newCall(request).execute();
    }

    public <R extends ErrorResponse> R post(String path, Class<R> responseClazz, Object input) {
        try (Response response = post(path, RequestBody.create(
            getObjectMapper().writeValueAsString(input), Constants.JSON
        ))) {
            if (!response.isSuccessful()) {
                if (response.body() == null) {
                    throw new ChromaAPIException("Response data is null");
                }
                R result = getObjectMapper().readValue(response.body().string(), responseClazz);
                result.setErrorCode(response.code());
                return result;
            }
            if (response.body() == null) {
                throw new ChromaAPIException("Response data is null");
            }
            String text = response.body().string();
            return getObjectMapper().readValue(text, responseClazz);
        } catch (IOException exception) {
            try {
                R result = responseClazz.getConstructor().newInstance();
                result.setErrorCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .setError(Constants.ERROR_IO)
                    .setMessage(exception.getMessage());
                return result;
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException
                     | IllegalAccessException e) {
                throw new ResponseInitializeException("Failed to init class " + responseClazz.getName());
            }
        }
    }


    public Response delete(String path) throws IOException {
        Request request = new Request.Builder()
            .url(baseUrl + path).delete()
            .headers(headers)
            .build();
        return client.newCall(request).execute();
    }


    public <R extends ErrorResponse> R delete(String path, Class<R> clazz) {
        try (Response response = delete(path)) {
            if (!response.isSuccessful()) {
                if (response.body() == null) {
                    throw new ChromaAPIException("Response data is null");
                }
                R result = getObjectMapper().readValue(response.body().string(), clazz);
                result.setErrorCode(response.code());
                return result;
            }
            if (response.body() == null) {
                throw new ChromaAPIException("Response data is null");
            }
            return getObjectMapper().readValue(response.body().string(), clazz);
        } catch (IOException exception) {
            try {
                R result = clazz.getConstructor().newInstance();
                result.setErrorCode(HttpStatus.SC_INTERNAL_SERVER_ERROR)
                    .setError(Constants.ERROR_IO)
                    .setMessage(exception.getMessage());
                return result;
            } catch (NoSuchMethodException | InvocationTargetException | InstantiationException
                     | IllegalAccessException e) {
                throw new ResponseInitializeException("Failed to init class " + clazz.getName());
            }
        }
    }
}
