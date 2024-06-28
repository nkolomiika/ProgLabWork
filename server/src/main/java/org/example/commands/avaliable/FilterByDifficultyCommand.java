package org.example.commands.avaliable;

import org.example.commands.abstarct.Command;
import org.example.managers.collection.CollectionManager;
import org.example.model.data.Difficulty;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.model.ArgumentType;
import org.example.network.model.Status;

public final class FilterByDifficultyCommand extends Command {

    private final CollectionManager collectionManager;

    public FilterByDifficultyCommand(CollectionManager collectionManager) {
        super("filter_by_difficulty", "вывести элементы, " +
                "значение поля difficulty которых равно заданному", ArgumentType.ENUM_DIFFICULTY);
        this.collectionManager = collectionManager;
    }

    @Override
    public Response execute(Request request) {
        String arg = request.getArgument().toUpperCase();
        var difficulty = Difficulty.valueOf(arg);
        return new Response(
                Status.OK,
                collectionManager.filterByDifficulty(difficulty)
        );
    }
}
