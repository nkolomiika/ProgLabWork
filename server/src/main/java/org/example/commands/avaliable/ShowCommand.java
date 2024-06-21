package org.example.commands.avaliable;

import org.example.commands.abstarct.Command;
import org.example.managers.collection.CollectionManager;
import org.example.managers.file.DumpManager;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.model.Status;

public final class ShowCommand extends Command {

    private final CollectionManager collectionManager;

    public ShowCommand(CollectionManager collectionManager) {
        super("show", "вывести все элементы коллекции");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        return new Response(
                Status.OK,
                collectionManager.convertCollectionToJson()
        );
    }
}
