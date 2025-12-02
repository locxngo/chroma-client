package io.github.locxngo.chroma.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class HealthCheckResponse extends ErrorResponse {
    @JsonProperty("is_executor_ready")
    private boolean isExecutorReady;
    @JsonProperty("is_log_client_ready")
    private boolean isLogClientReady;
}
