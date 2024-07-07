package org.example.commands.avaliable;

import org.example.commands.abstarct.Command;
import org.example.managers.collection.CollectionManager;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.model.ArgumentType;
import org.example.network.model.Status;

public final class AddCommand extends Command {
    private final CollectionManager collectionManager;

    public AddCommand(CollectionManager collectionManager) {
        super("add", "добавить новый элемент в коллекцию", ArgumentType.LAB_WORK);
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        this.collectionManager.add(request.getLabWork());
        return new Response(Status.OK, "Lab successfully added!");
    }
}