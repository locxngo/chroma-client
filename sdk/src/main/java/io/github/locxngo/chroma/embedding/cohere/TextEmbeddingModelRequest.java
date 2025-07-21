package io.github.locxngo.chroma.embedding.cohere;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.locxngo.chroma.embedding.EmbeddingModelRequest;
import io.github.locxngo.chroma.embedding.Dtype;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
public class TextEmbeddingModelRequest extends EmbeddingModelRequest {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> texts;
    private List<String> images;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String model;
    @JsonProperty("input_type")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private InputType inputType;
    @JsonProperty("max_tokens")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int maxTokens;
    @JsonProperty("output_dimension")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int outputDimensions;
    @JsonProperty("embedding_types")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<Dtype> embeddingTypes;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Truncate truncate;
}
