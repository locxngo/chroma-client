package io.github.locxngo.chroma.embedding.voyageai;

import io.github.locxngo.chroma.schemas.PrintableObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import java.util.List;

@Setter
@Getter
@Accessors(chain = true)
public class TextEmbeddingModelResponse<N> extends PrintableObject {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String object;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String model;
    private List<Data<N>> data;
    private Usage usage;

    @Setter
    @Getter
    @Accessors(chain = true)
    public static class Data<N> extends PrintableObject {
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private String object;
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private int index;
        private List<N> embedding;
    }

    @Setter
    @Getter
    @Accessors(chain = true)
    public static class Usage extends PrintableObject {
        @JsonProperty("total_tokens")
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private int totalTokens;
    }

    public static class FloatDtype extends TextEmbeddingModelResponse<Float> {
    }

    public static class IntDtype extends TextEmbeddingModelResponse<Integer> {
    }

    public static class ByteDtype extends TextEmbeddingModelResponse<Integer> {
    }
}
