package org.example.exceptions.process;

public class CannotAddLabWorkRuntimeException extends RuntimeException {
    public CannotAddLabWorkRuntimeException() {
        super("Something went wrong. Lab didn't add :(");
    }
}
