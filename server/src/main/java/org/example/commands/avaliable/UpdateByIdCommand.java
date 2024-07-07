package org.example.commands.avaliable;

import org.example.commands.abstarct.Command;
import org.example.exceptions.collection.InvalidLabIdException;
import org.example.managers.collection.CollectionManager;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.model.ArgumentType;
import org.example.network.model.Status;

public final class UpdateByIdCommand extends Command {
    private final CollectionManager collectionManager;

    public UpdateByIdCommand(CollectionManager collectionManager) {
        super(
                "update_by_id",
                "обновить значение элемента коллекции, id которого равен заданному",
                ArgumentType.NUMBER_AND_LAB_WORK
        );
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) throws InvalidLabIdException {
        long id = Long.parseLong(request.getArgument());
        this.collectionManager.update(id, request.getLabWork());
        return new Response(
                Status.OK,
                "Element with id=" + id + " successfully updated!"
        );
    }
}
