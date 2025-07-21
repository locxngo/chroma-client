package io.github.locxngo.chroma.embedding.cohere;

import io.github.locxngo.chroma.schemas.PrintableObject;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class ErrorResponse extends PrintableObject {
    private String id;
    private String message;
}
