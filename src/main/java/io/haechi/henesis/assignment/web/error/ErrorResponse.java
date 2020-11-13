package io.haechi.henesis.assignment.web.error;

public class ErrorResponse {
    private ErrorBody error;

    public ErrorResponse(ErrorBody error) {
        this.error = error;
    }

    public ErrorBody getError() {
        return this.error;
    }

    public void setError(ErrorBody error) {
        this.error = error;
    }
}
