package org.example;

import lombok.SneakyThrows;
import org.example.managers.threads.FutureManager;
import org.example.network.ServerTCP;
import org.example.runners.MainRunner;

import java.util.concurrent.ForkJoinPool;

/**
 * Hello world!
 */
public class Server {
    @SneakyThrows
    public static void main(String[] args) {
        ServerTCP.acceptConnection();
        MainRunner runner = new MainRunner();

        while (true) {
            try {
                runner.run();
            } catch (Exception exception) {
                break;
            }
        }
    }
}
