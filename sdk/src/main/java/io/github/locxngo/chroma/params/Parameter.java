package io.github.locxngo.chroma.params;

import io.github.locxngo.chroma.schemas.ErrorResponse;
import io.github.locxngo.chroma.schemas.PrintableObject;

public abstract class Parameter extends PrintableObject {
    public abstract ErrorResponse validate();
}
