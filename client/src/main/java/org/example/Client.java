package org.example;


import lombok.SneakyThrows;
import org.example.managers.RunnerManager;
import org.example.network.dto.User;
import org.example.network.model.RuntimeMode;
import org.example.utils.builders.avaliable.UserBuilder;
import org.example.utils.io.console.Console;

import java.util.NoSuchElementException;

/**
 * Hello world!
 */
public class Client {
    private static RuntimeMode runtimeMode;
    private static User user;
    private static final RunnerManager runnerManager;

    static {
        user = UserBuilder.buildEmptyUser();
        runtimeMode = RuntimeMode.AUTH;
        runnerManager = new RunnerManager();
    }

    @SneakyThrows
    public static void main(String[] args) {
        boolean isRunning = true;

        while (isRunning) {
            try {
                var pair = runnerManager.launchRunner(runtimeMode, user);
                runtimeMode = pair.runtimeMode();
                user = pair.user();
            } catch (NoSuchElementException exception) {
                Console.println(exception.getMessage());
                isRunning = false;
            }
        }
    }
}
