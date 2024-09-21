package org.example.exceptions.process;

import lombok.Getter;
import org.example.network.model.RuntimeMode;

public class ExitObligedRuntimeException extends RuntimeException{
    @Getter
    private RuntimeMode runtimeMode;
    public ExitObligedRuntimeException(String message, RuntimeMode runtimeMode){
        super(message);
        this.runtimeMode = runtimeMode;
    }
}
