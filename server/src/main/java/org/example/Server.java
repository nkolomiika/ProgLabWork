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
