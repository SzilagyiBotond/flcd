package org.example;

public class LexicalError extends RuntimeException {
    public LexicalError(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
