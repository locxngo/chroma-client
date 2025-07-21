package io.github.locxngo.chroma.types;

import lombok.Getter;

import java.util.List;
import java.util.stream.Stream;

@Getter
public enum Include {
    URIS("uris"),
    METADATAS("metadatas"),
    EMBEDDINGS("embeddings"),
    DISTANCES("distances"),
    DOCUMENTS("documents");
    private final String value;

    Include(String value) {
        this.value = value;
    }

    public static List<String> defaultList() {
        return Stream.of(DOCUMENTS, METADATAS).map(Include::getValue).toList();
    }
}
