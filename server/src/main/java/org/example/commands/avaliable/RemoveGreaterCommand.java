package org.example.commands.avaliable;

import org.example.commands.abstarct.Command;
import org.example.exceptions.collection.EmptyCollectionException;
import org.example.managers.collection.CollectionManager;
import org.example.model.data.LabWork;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.model.ArgumentType;
import org.example.network.model.Status;

public final class RemoveGreaterCommand extends Command {
    private final CollectionManager collectionManager;

    public RemoveGreaterCommand(CollectionManager collectionManager) {
        super("remove_greater",
                "удалить из коллекции все элементы, превышающие заданный",
                ArgumentType.LAB_WORK);
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) throws EmptyCollectionException {
        LabWork lab = request.getLabWork();
        collectionManager.removeGreater(lab);
        return new Response(
                Status.OK,
                "Labs successfully removed with minimal point greater, than " + lab.getMinimalPoint()
        );
    }
}
