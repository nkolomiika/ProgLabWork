package org.example.commands.avaliable.runtime;

import org.example.commands.abstarct.Command;
import org.example.managers.collection.CollectionManager;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.model.Status;

import java.sql.SQLException;

public final class ClearCommand extends Command {

    private final CollectionManager collectionManager;

    public ClearCommand(CollectionManager collectionManager) {
        super("clear", "очистить коллекцию");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) throws RuntimeException {
        collectionManager.clearCollection(request.getUser());
        return new Response(Status.OK, "Collection successfully cleared!");
    }
}
