package org.example.commands.avaliable.runtime;

import org.example.commands.abstarct.Command;
import org.example.managers.collection.CollectionManager;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.model.ArgumentType;
import org.example.network.model.Status;

import java.sql.SQLException;

public final class RemoveByIdCommand extends Command {
    private final CollectionManager collectionManager;

    public RemoveByIdCommand(CollectionManager collectionManager) {
        super("remove_by_id", "удалить элемент по id", ArgumentType.NUMBER);
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) throws RuntimeException {
        int id = Integer.parseInt(request.getArgument());
        collectionManager.removeById(id, request.getUser());
        return new Response(Status.OK, "Element successfully deleted with id=" + id);
    }
}
