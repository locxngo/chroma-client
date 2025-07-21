package io.github.locxngo.chroma.schemas;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.Map;

@Setter
@Getter
@Accessors(chain = true)
public class EmbeddingFunctionConfig extends PrintableObject {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String type;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String name;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private Map<String, Object> config;
}
