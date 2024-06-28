package org.example.exceptions.file;

public class FindRecursionRuntimeException extends RuntimeException {
    public FindRecursionRuntimeException() {
        super("Ooops! execute_script in file has ignored :(");
    }
}
