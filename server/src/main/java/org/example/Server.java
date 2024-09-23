package org.example;

import lombok.SneakyThrows;
import org.example.network.ServerTCP;
import org.example.runners.MainRunner;

/**
 * Hello world!
 */
public class Server {
    @SneakyThrows
    public static void main(String[] args) {
        MainRunner runner = new MainRunner();
        ServerTCP.acceptConnection();

        while (true) {
            runner.run();
        }
    }
}
