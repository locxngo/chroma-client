package io.github.locxngo.chroma.embedding.openai;

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
public class TextEmbeddingModelResponse extends PrintableObject {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String object;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String model;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Data> data;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Usage usage;

    @Setter
    @Getter
    @Accessors(chain = true)
    public static class Data extends PrintableObject {
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private String object;
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private int index;
        private List<Float> embedding;
    }

    @Setter
    @Getter
    @Accessors(chain = true)
    public static class Usage extends PrintableObject {
        @JsonProperty("total_tokens")
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private int totalTokens;
        @JsonProperty("prompt_tokens")
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private int promptTokens;
    }
}
