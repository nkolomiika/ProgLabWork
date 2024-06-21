package org.example.exceptions.input;

public class EmptyStringRuntimeException extends RuntimeException {
    public EmptyStringRuntimeException() {
        super("Oops! String cannot be empty :(");
    }
}
