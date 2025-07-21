package io.github.locxngo.chroma;

import io.github.locxngo.chroma.exception.ChromaAPIException;
import io.github.locxngo.chroma.schemas.ChecklistResponse;
import io.github.locxngo.chroma.schemas.CountResponse;
import io.github.locxngo.chroma.schemas.ErrorResponse;
import io.github.locxngo.chroma.schemas.GetUserIdentityResponse;
import io.github.locxngo.chroma.schemas.HealthCheckResponse;
import io.github.locxngo.chroma.schemas.HeartbeatResponse;
import io.github.locxngo.chroma.schemas.ResetResponse;
import io.github.locxngo.chroma.schemas.VersionResponse;
import lombok.Getter;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;

@Getter
public class BaseClient {

    private final ApiClient apiClient;

    public BaseClient(Config config) {
        this.apiClient = new ApiClient(
            config.getEndpoint(),
            config.getParameters()
        );
    }

    protected <T extends ErrorResponse> T badParameter(T response, String message) {
        response.setErrorCode(HttpStatus.SC_BAD_REQUEST)
            .setError(Constants.ERROR_PARAMETER)
            .setMessage(message);
        return response;
    }

    protected <T extends ErrorResponse> T ioException(T response, String message) {
        response.setErrorCode(HttpStatus.SC_BAD_REQUEST)
            .setError(Constants.ERROR_IO)
            .setMessage(message);
        return response;
    }

    /**
     * Retrieves the current user's identity, tenant, and databases.
     */
    public GetUserIdentityResponse identity() {
        return apiClient.get(ApiPath.IDENTITY, GetUserIdentityResponse.class);
    }

    /**
     * Resets the database. This will delete all collections and entries.
     */
    public ResetResponse reset() {
        try (Response response = getApiClient().post(ApiPath.VERSION, RequestBody.create("", Constants.TEXT))) {
            if (!response.isSuccessful()) {
                if (response.body() == null) {
                    throw new ChromaAPIException("Response data is null");
                }
                return (ResetResponse) new ResetResponse()
                    .setErrorCode(response.code())
                    .setError("").setMessage(response.message());
            }
            if (response.body() == null) {
                throw new ChromaAPIException("Response data is null");
            }
            return new ResetResponse()
                .setSuccess(Boolean.parseBoolean(response.body().string().replace("\"", "")));
        } catch (IOException exception) {
            return ioException(new ResetResponse(), exception.getMessage());
        }
    }

    /**
     * Health check endpoint that returns 200 if the server and executor are ready
     */
    public HealthCheckResponse healthCheck() {
        return apiClient.get(ApiPath.HEALTH_CHECK, HealthCheckResponse.class);
    }

    /**
     * Get the current time in nanoseconds since epoch. Used to check if the server is alive.
     */
    public HeartbeatResponse heartBeat() {
        return apiClient.get(ApiPath.HEART_BEAT, HeartbeatResponse.class);
    }

    /**
     * Return the maximum number of records that can be created or mutated in a single call.
     */
    public ChecklistResponse preFlightChecks() {
        return apiClient.get(ApiPath.PRE_FLIGHT_CHECKS, ChecklistResponse.class);
    }

    /**
     * Get the version of Chroma.
     */
    public VersionResponse version() {
        try (Response response = getApiClient().get(ApiPath.VERSION)) {
            if (!response.isSuccessful()) {
                if (response.body() == null) {
                    throw new ChromaAPIException("Response data is null");
                }
                return (VersionResponse) getApiClient().getObjectMapper()
                    .readValue(response.body().string(), CountResponse.class)
                    .setErrorCode(response.code());
            }
            if (response.body() == null) {
                throw new ChromaAPIException("Response data is null");
            }
            return new VersionResponse()
                .setVersion(response.body().string().replace("\"", ""));
        } catch (IOException exception) {
            return ioException(new VersionResponse(), exception.getMessage());
        }
    }
}
