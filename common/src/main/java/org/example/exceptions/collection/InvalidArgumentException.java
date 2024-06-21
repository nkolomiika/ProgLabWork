package org.example.exceptions.collection;

public class InvalidArgumentException extends RuntimeException {
    public InvalidArgumentException(){
        super("Ooops! Invalid argument format :(");
    }

    public InvalidArgumentException(String message) {
        super(message);
    }
}
