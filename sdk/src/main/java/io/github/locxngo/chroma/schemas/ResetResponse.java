package io.github.locxngo.chroma.schemas;

import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

@Setter
@Getter
@Accessors(chain = true)
public class ResetResponse extends ErrorResponse {
    private boolean success;
}
