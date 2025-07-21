package io.github.locxngo.chroma.schemas;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class ForkCollectionPayload extends PrintableObject {
    @JsonProperty("new_name")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String newName;
}
