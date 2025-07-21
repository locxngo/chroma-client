package io.github.locxngo.chroma.embedding.cohere;

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
public class TextEmbeddingModelResponse extends PrintableObject {
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String id;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Embeddings embeddings;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<String> texts;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<Image> images;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Meta meta;

    @Setter
    @Getter
    @Accessors(chain = true)
    public static class Image {
        @JsonProperty("width")
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private long width;
        @JsonProperty("height")
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private long height;
        @JsonProperty("format")
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private String format;
        @JsonProperty("bit_depth")
        @JsonInclude(JsonInclude.Include.NON_DEFAULT)
        private long bitDepth;
    }

    @Setter
    @Getter
    @Accessors(chain = true)
    public static class Meta {
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private List<String> warnings;
        @JsonProperty("api_version")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private ApiVersion apiVersion;
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Tokens tokens;
        @JsonProperty("billed_units")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private BilledUnits billedUnits;
    }

    @Setter
    @Getter
    @Accessors(chain = true)
    public static class ApiVersion {
        @JsonProperty("version")
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private String version;
        @JsonProperty("is_deprecated")
        private boolean isDeprecated;
        @JsonProperty("is_experimental")
        private boolean isExperimental;
    }

    @Setter
    @Getter
    @Accessors(chain = true)
    public static class Tokens {
        @JsonProperty("input_tokens")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Double inputTokens;
        @JsonProperty("output_tokens")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Double outputTokens;
    }

    @Setter
    @Getter
    @Accessors(chain = true)
    public static class BilledUnits {
        @JsonProperty("images")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Double images;
        @JsonProperty("input_tokens")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Double inputTokens;
        @JsonProperty("output_tokens")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Double outputTokens;
        @JsonProperty("search_units")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Double searchUnits;
        @JsonProperty("classifications")
        @JsonInclude(JsonInclude.Include.NON_NULL)
        private Double classifications;
    }

    @Setter
    @Getter
    @Accessors(chain = true)
    public static class Embeddings {
        @JsonProperty("float")
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private List<List<Float>> floats;
        @JsonProperty("int8")
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private List<List<Integer>> int8;
        @JsonProperty("uint8")
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private List<List<Integer>> uint8;
        @JsonProperty("binary")
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private List<List<Integer>> binary;
        @JsonProperty("ubinary")
        @JsonInclude(JsonInclude.Include.NON_EMPTY)
        private List<List<Integer>> ubinary;
    }

}
