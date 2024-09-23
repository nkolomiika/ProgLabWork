package org.example.commands.avaliable.runtime;

import org.example.commands.abstarct.Command;
import org.example.managers.collection.CollectionManager;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.model.Status;

public final class SumOfMinimalPointCommand extends Command {
    private final CollectionManager collectionManager;

    public SumOfMinimalPointCommand(CollectionManager collectionManager) {
        super("sum_of_minimal_point",
                "вывести сумму minimal-point всех элементов коллекции");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        long sum = collectionManager.sumOfMinimumPoint();
        return new Response(
                Status.OK,
                "Lab Work`s minimal points sum is: " + sum
        );
    }
}
