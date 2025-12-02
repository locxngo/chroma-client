package chroma.example;

import io.github.locxngo.chroma.BaseClient;
import io.github.locxngo.chroma.Config;
import io.github.locxngo.chroma.schemas.ChecklistResponse;
import io.github.locxngo.chroma.schemas.ErrorResponse;
import io.github.locxngo.chroma.schemas.GetUserIdentityResponse;
import io.github.locxngo.chroma.schemas.HealthCheckResponse;
import io.github.locxngo.chroma.schemas.HeartbeatResponse;
import io.github.locxngo.chroma.schemas.VersionResponse;

public class BaseAPI {

    @SuppressWarnings("java:S112")
    public static <E extends ErrorResponse> void throwErrorIfHas(E response) {
        if (response == null) {
            throw new RuntimeException("Response is null");
        }
        if (response.isError()) {
            throw new RuntimeException(
                    "code=" + response.getErrorCode() + ", err=" + response.getError() + ", msg=" + response.getMessage()
            );
        }
    }

    @SuppressWarnings("java:S106")
    public static void main(String[] args) {
        BaseClient client = new BaseClient(
                Config.builder().build()
        );
        VersionResponse versionResponse = client.version();
        throwErrorIfHas(versionResponse);
        HealthCheckResponse healthCheckResponse = client.healthCheck();
        throwErrorIfHas(healthCheckResponse);

        HeartbeatResponse heartbeatResponse = client.heartBeat();
        throwErrorIfHas(heartbeatResponse);

        GetUserIdentityResponse identityResponse = client.identity();
        throwErrorIfHas(identityResponse);

        ChecklistResponse checklistResponse = client.preFlightChecks();
        throwErrorIfHas(checklistResponse);

        System.out.println("version: " + versionResponse.getVersion());
        System.out.println("is_log_client_ready: " + healthCheckResponse.isLogClientReady());
        System.out.println("is_executor_ready: " + healthCheckResponse.isExecutorReady());
        System.out.println("heartbeat_nanoseconds: " + heartbeatResponse.getHeartbeatNanoseconds());
        System.out.println("user_id: " + identityResponse.getUserId());
        System.out.println("tenant: " + identityResponse.getTenant());
        System.out.println("databases: " + identityResponse.getDatabases());
        System.out.println("max_batch_size: " + checklistResponse.getMaxBatchSize());
        System.out.println("is_support_base64_encoding: " + checklistResponse.isSupportsBase64Encoding());
    }
}
