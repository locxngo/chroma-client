package io.github.locxngo.chroma.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class ChecklistResponse extends ErrorResponse {
    @JsonProperty("max_batch_size")
    private int maxBatchSize;
    @JsonProperty("supports_base64_encoding")
    private boolean supportsBase64Encoding;
}
