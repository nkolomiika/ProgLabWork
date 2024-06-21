package org.example.commands.avaliable;

import org.example.commands.abstarct.Command;
import org.example.exceptions.collection.EmptyCollectionException;
import org.example.managers.collection.CollectionManager;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.model.Status;

public final class RemoveFirstCommand extends Command {

    private final CollectionManager collectionManager;

    public RemoveFirstCommand(CollectionManager collectionManager) {
        super("remove_first", "удалить первый элемент из коллекции");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) throws EmptyCollectionException {
        collectionManager.removeFirst();
        return new Response(Status.OK, "Lab Work successfully deleted!");
    }
}
