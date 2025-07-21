package io.github.locxngo.chroma.exception;

public class CreateEmbeddingException extends RuntimeException {

    public CreateEmbeddingException(String message, Throwable cause) {
        super(message, cause);
    }

    public CreateEmbeddingException(Throwable cause) {
        super(cause);
    }

    public CreateEmbeddingException(String message) {
        super(message);
    }
}
