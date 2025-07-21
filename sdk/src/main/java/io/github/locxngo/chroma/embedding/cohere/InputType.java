package io.github.locxngo.chroma.embedding.cohere;

import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

@Getter
public enum InputType {
    SEARCH_DOCUMENT("search_document"),
    SEARCH_QUERY("search_query"),
    CLASSIFICATION("classification"),
    CLUSTERING("clustering"),
    IMAGE("image");
    @JsonValue
    private final String value;

    InputType(String value) {
        this.value = value;
    }
}
