package org.example.exceptions.input;

public class InvalidInputFormatException extends RuntimeException{
    public InvalidInputFormatException(){
        super("Ooops! Invalid input format :(");
    }
}
