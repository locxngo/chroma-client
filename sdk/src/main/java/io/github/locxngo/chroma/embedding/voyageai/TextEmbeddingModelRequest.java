package io.github.locxngo.chroma.embedding.voyageai;

import io.github.locxngo.chroma.embedding.EmbeddingModelRequest;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
public class TextEmbeddingModelRequest extends EmbeddingModelRequest {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> input;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String model;
    @JsonProperty("input_type")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private InputType inputType;
    private boolean truncation;
    @JsonProperty("output_dimension")
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int outputDimension;
    @JsonProperty("output_dtype")
    private Dtype outputDtype = Dtype.FLOAT;
    @JsonProperty("encoding_format")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String encodingFormat;
}
