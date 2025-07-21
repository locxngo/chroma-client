package io.github.locxngo.chroma.schemas;

import io.github.locxngo.chroma.types.Metadata;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class CreateCollectionPayload extends PrintableObject {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private CollectionConfiguration configuration;
    @JsonProperty("get_or_create")
    private boolean getOrCreate;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Metadata metadata;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String name;
}
