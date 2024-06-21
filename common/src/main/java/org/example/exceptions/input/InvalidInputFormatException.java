package org.example.exceptions.input;

public class InvalidInputFormatException extends RuntimeException{
    public InvalidInputFormatException(){
        super("Oops! Invalid input format :(");
    }
}
