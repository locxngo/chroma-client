package io.github.locxngo.chroma.embedding.mistral;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.locxngo.chroma.embedding.Dtype;
import io.github.locxngo.chroma.embedding.EmbeddingModelRequest;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
public class TextEmbeddingModelRequest extends EmbeddingModelRequest {
    @JsonProperty("model")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String model;
    @JsonProperty("input")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> input;
    @JsonProperty("output_type")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Dtype outputType = Dtype.FLOAT;
    @JsonProperty("output_dimension")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int outputDimension;
}
