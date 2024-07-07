package org.example.commands.avaliable;

import org.example.commands.abstarct.Command;
import org.example.managers.collection.CollectionManager;
import org.example.network.dto.Request;
import org.example.network.dto.Response;

// todo дописать класс
public final class PrintUniqueDifficultyCommand extends Command {
    private final CollectionManager collectionManager;

    public PrintUniqueDifficultyCommand(CollectionManager collectionManager) {
        super("print_unique_difficulty",
                "вывести lab work с уникальным значением difficulty");
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        return null;
    }
}
