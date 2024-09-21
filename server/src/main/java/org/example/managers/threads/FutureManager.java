package org.example.managers.threads;

import org.example.network.ServerTCP;
import org.example.network.dto.Response;
import org.example.utils.io.console.Console;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class FutureManager {
    private static final Collection<Future<Response>> cashedThreadPoolFutures = new ArrayList<>();

    public static void addNewFixedThreadPoolFuture(Future<Response> future) {
        cashedThreadPoolFutures.add(future);
    }

    public static void checkAllFutures() {

        if (!cashedThreadPoolFutures.isEmpty()) {
            cashedThreadPoolFutures.forEach(s -> Console.println(s.toString()));
        }
        cashedThreadPoolFutures.stream()
                .filter(Future::isDone)
                .forEach(future -> {
                    try {
                        ServerTCP.sendResponse(future.get());
                    } catch (InterruptedException | ExecutionException e) {
                        Console.printError(e.getMessage());
                    }
                });
        cashedThreadPoolFutures.removeIf(Future::isDone);
    }
}