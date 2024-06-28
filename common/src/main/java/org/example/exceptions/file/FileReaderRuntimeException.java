package org.example.exceptions.file;

import java.io.IOException;

public class FileReaderRuntimeException extends IOException {
    public FileReaderRuntimeException() {
        super("Ooops! Something went wrong while reading lab work data :(");
    }
}
