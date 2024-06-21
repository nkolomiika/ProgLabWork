package org.example.commands.avaliable;

import org.example.commands.abstarct.Command;
import org.example.managers.collection.CollectionManager;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.model.Status;

public final class AddIfMinCommand extends Command {
    public CollectionManager collectionManager;

    public AddIfMinCommand(CollectionManager collectionManager) {
        super("add_if_min", "добавить новый элемент в коллекцию," +
                " если его значение меньше, чем у наименьшего элемента этой коллекции");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        if (this.collectionManager.addIfMin(request.getLabWork()))
            return new Response(Status.OK, "Lab successfully added!");

        return new Response(Status.ERROR, "Something went wrong. Lab didn't add :(");
    }
}
