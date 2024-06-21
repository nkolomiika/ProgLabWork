package org.example.exceptions.input;

public class InputRuntimeException extends RuntimeException {
    public InputRuntimeException() {
        super("Ooops! Something went wrong :(");
    }
}
