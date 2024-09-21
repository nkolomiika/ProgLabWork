package org.example;


import lombok.SneakyThrows;
import org.example.managers.ArgumentParserManager;
import org.example.managers.RunnerManager;
import org.example.network.model.RuntimeMode;
import org.example.utils.io.console.Console;

import java.util.NoSuchElementException;

/**
 * Hello world!
 */
public class Client {
    private static RuntimeMode runtimeMode;
    private static final RunnerManager runnerManager;

    static {
        runtimeMode = RuntimeMode.CONSOLE;
        runnerManager = new RunnerManager();
    }

    @SneakyThrows
    public static void main(String[] args) {
        boolean isRunning = true;

        while (isRunning) {
            try {
                runtimeMode = runnerManager.launchRunner(runtimeMode, null);
            } catch (NoSuchElementException exception) {
                Console.println(exception.getMessage());
                isRunning = false;
            }
        }
    }
}
