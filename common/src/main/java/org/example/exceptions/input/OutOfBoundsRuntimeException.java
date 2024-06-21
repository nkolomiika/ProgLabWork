package org.example.exceptions.input;

public class OutOfBoundsRuntimeException extends RuntimeException {
    public OutOfBoundsRuntimeException(int bound) {
        super("Ooops! Value out of bounds. Max value is " + bound);
    }
}
