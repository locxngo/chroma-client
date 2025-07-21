package io.github.locxngo.chroma.schemas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

@Setter
@Getter
@Accessors(chain = true)
public class UpdateCollectionPayload extends PrintableObject {
    @JsonProperty("new_metadata")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> metadata;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String name;
}
