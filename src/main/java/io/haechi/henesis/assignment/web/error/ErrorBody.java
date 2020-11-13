package io.haechi.henesis.assignment.web.error;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ErrorBody {
    private String message;
    private int code;

    public ErrorBody(String message, int code) {
        this.message = message;
        this.code = code;
    }
}
