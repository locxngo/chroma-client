package io.github.locxngo.chroma.schemas;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;

@Setter
@Getter
@Accessors(chain = true)
public class ErrorResponse extends PrintableObject {
    @JsonIgnore
    private int errorCode = 0;
    @JsonProperty("error")
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String error;
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    @JsonProperty("message")
    private String message;

    /**
     * returns {@code true} if it is error, otherwise returns {@code false}
     */
    public boolean isError() {
        return errorCode > 0 || StringUtils.isNoneBlank(error);
    }
}
