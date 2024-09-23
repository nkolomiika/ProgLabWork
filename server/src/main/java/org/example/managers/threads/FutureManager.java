package org.example.managers.threads;

import org.example.network.ServerTCP;
import org.example.network.dto.Response;
import org.example.utils.io.console.Console;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


public class FutureManager {
    private static final Collection<Future<Response>> cachedThreadPoolFutures;

    static {
        cachedThreadPoolFutures = new ArrayList<>();
    }

    public static void checkAllFutures() {

        if (!cachedThreadPoolFutures.isEmpty()) {
            cachedThreadPoolFutures.forEach(s -> Console.println(s.toString()));
        }
        cachedThreadPoolFutures.stream()
                .filter(Future::isDone)
                .forEach(future -> {
                    try {
                        ServerTCP.sendResponse(future.get());
                    } catch (InterruptedException | ExecutionException e) {
                        Console.printError(e.getMessage());
                    }
                });
        cachedThreadPoolFutures.removeIf(Future::isDone);
    }
}