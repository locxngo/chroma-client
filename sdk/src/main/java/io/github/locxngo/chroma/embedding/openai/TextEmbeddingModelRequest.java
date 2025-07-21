package io.github.locxngo.chroma.embedding.openai;

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
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int dimensions;
    @JsonProperty("encoding_format")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String encodingFormat;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String user;

}
