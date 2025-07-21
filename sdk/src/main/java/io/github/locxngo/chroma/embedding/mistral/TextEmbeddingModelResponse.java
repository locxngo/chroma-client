package io.github.locxngo.chroma.embedding.mistral;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.github.locxngo.chroma.schemas.PrintableObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
public class TextEmbeddingModelResponse<N> extends PrintableObject {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String id;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String model;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Usage usage;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Item<N>> data;

    @Setter
    @Getter
    @Accessors(chain = true)
    public static class Usage {
        @JsonProperty("prompt_tokens")
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private int promptTokens;
        @JsonProperty("completion_tokens")
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private int completionTokens;
        @JsonProperty("total_tokens")
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private int totalTokens;
    }

    @Setter
    @Getter
    @Accessors(chain = true)
    public static class Item<N> {
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private String object;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private int index;
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private List<N> embedding;
    }

    public static class OutputFloat extends TextEmbeddingModelResponse<Float> {

    }

    public static class OutputInt extends TextEmbeddingModelResponse<Integer> {

    }

    public static class OutputBinary extends TextEmbeddingModelResponse<Integer> {

    }
}
