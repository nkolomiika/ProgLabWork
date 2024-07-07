package org.example.commands.avaliable;

import org.example.commands.abstarct.Command;
import org.example.exceptions.process.CannotAddLabWorkRuntimeException;
import org.example.managers.collection.CollectionManager;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.model.ArgumentType;
import org.example.network.model.Status;

public final class AddIfMinCommand extends Command {
    public CollectionManager collectionManager;

    public AddIfMinCommand(CollectionManager collectionManager) {
        super("add_if_min", "добавить новый элемент в коллекцию," +
                " если его значение меньше, чем у наименьшего элемента этой коллекции", ArgumentType.LAB_WORK);
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) throws CannotAddLabWorkRuntimeException {
        this.collectionManager.addIfMin(request.getLabWork());
        return new Response(Status.OK, "Lab successfully added!");
    }
}
