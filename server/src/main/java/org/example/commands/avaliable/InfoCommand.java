package org.example.commands.avaliable;

import org.example.commands.abstarct.Command;
import org.example.managers.collection.CollectionManager;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.model.Status;

public final class InfoCommand extends Command {
    private final CollectionManager collectionManager;

    public InfoCommand(CollectionManager collectionManager) {
        super("info", "вывести в стандартный поток вывода информацию о коллекции");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        return new Response(Status.OK, collectionManager.info());
    }
}
