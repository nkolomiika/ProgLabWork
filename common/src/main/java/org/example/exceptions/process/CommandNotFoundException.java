package org.example.exceptions.process;

public class CommandNotFoundException extends RuntimeException{
    public CommandNotFoundException() {
        super("Ooops! Cannot find this command :(");
    }
}
