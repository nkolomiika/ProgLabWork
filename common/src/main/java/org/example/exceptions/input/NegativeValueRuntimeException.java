package org.example.exceptions.input;

public class NegativeValueRuntimeException extends RuntimeException {
    public NegativeValueRuntimeException() {
        super("Ooops! Value must be positive :(");
    }
}
