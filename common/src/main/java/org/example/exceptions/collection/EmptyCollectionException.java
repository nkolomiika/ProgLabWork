package org.example.exceptions.collection;

public class EmptyCollectionException extends RuntimeException{
    public EmptyCollectionException(){
        super("Ooops! Collection is empty :(");
    }
}
