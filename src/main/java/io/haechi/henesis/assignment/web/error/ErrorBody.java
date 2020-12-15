package io.haechi.henesis.assignment.web.error;

public class ErrorBody {
    private String message;
    private int code;

    public ErrorBody(String message, int code) {
        this.message = message;
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
