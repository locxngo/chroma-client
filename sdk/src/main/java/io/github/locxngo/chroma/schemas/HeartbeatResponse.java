package io.github.locxngo.chroma.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class HeartbeatResponse extends ErrorResponse {
    @JsonProperty("nanosecond heartbeat")
    private long heartbeatNanoseconds;
}
