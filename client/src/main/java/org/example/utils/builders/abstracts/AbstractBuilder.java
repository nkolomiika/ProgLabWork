package org.example.utils.builders.abstracts;

import org.example.runners.RuntimeMode;
import org.example.utils.io.file.CustomFileReader;

public abstract class AbstractBuilder {
    protected static CustomFileReader fileReader;
    protected static RuntimeMode runtimeMode;

    protected static void setFileReader(CustomFileReader fileReader) {
        AbstractBuilder.fileReader = fileReader;
    }

    public static void setRuntimeMode(RuntimeMode runtimeMode){
        AbstractBuilder.runtimeMode = runtimeMode;
    }
}
