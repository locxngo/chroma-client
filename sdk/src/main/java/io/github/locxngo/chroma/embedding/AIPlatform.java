package io.github.locxngo.chroma.embedding;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;
import io.github.locxngo.chroma.exception.NotFoundValueException;
import lombok.Getter;

import java.util.stream.Stream;

@Getter
public enum AIPlatform {
    OPENAI("openai"),
    VOYAGE_AI("voyageai"),
    COHERE("cohere"),
    MISTRAL("mistral"),
    GOOGLE_GEMINI("google_gemini"),
    OLLAMA("ollama");

    @JsonValue
    private final String value;

    AIPlatform(String value) {
        this.value = value;
    }

    @JsonCreator
    public static AIPlatform fromValue(String value) {
        return Stream.of(AIPlatform.values())
                .filter(status -> status.getValue().equals(value))
                .findFirst().orElseThrow(() -> new NotFoundValueException("not found enum Model for value " + value));
    }

    public String getApiKeyFromEnv() {
        return System.getenv(getValue().toUpperCase() + "_API_KEY");
    }
}
