package io.github.locxngo.chroma;

import okhttp3.MediaType;

public class Constants {
    private Constants() {
    }

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    public static final MediaType TEXT = MediaType.parse("text/plain");
    public static final String ERROR_IO = "ioexception";
    public static final String ERROR_PARAMETER = "parameter";
}
