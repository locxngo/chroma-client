package io.github.locxngo.chroma.embedding.openai;

import io.github.locxngo.chroma.schemas.PrintableObject;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class EmbeddingErrorResponse extends PrintableObject {
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private Error error;

    public static class Error {
        private String message;
        private String type;
    }
}
