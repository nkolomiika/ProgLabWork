package org.example.exceptions.collection;

public class InvalidLabIdException extends RuntimeException{
    public InvalidLabIdException(){
        super("Ooops! Cannot find lab with this id :(");
    }
}
