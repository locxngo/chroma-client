package io.github.locxngo.chroma.exception;

public class ChromaConnectException extends RuntimeException {
    public ChromaConnectException(String message) {
        super(message);
    }

    public ChromaConnectException(Throwable cause) {
        super(cause);
    }
}
