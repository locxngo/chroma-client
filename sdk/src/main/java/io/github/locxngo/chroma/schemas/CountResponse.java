package io.github.locxngo.chroma.schemas;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class CountResponse extends ErrorResponse {
    @JsonProperty("number_of_records")
    private long numberOfRecords;
}
