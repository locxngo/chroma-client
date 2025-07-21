package io.github.locxngo.chroma.schemas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
public class GetUserIdentityResponse extends ErrorResponse {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> databases;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String tenant;
    @JsonProperty("user_id")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String userId;
}
