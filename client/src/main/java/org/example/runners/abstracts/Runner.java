package org.example.runners.abstracts;

import lombok.Getter;
import org.example.exceptions.collection.InvalidArgumentException;
import org.example.exceptions.process.CommandNotFoundException;
import org.example.managers.ArgumentParserManager;
import org.example.network.dto.User;
import org.example.network.model.ArgumentType;
import org.example.network.model.RuntimeMode;
import org.example.runners.Pair;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

public abstract class Runner {
    @Getter
    protected RuntimeMode runtimeMode;
    protected Map<String, ArgumentType> availableCommands;

    public abstract Pair run(User user) throws IOException, NoSuchElementException;

    protected boolean isInputValid(String command, String argument)
            throws RuntimeException {
        ArgumentType argumentType = availableCommands.get(command);
        if (argumentType == null) throw new CommandNotFoundException();
        if (!isArgumentValid(argumentType, argument)) throw new InvalidArgumentException();
        return true;
    }

    protected boolean isArgumentValid(ArgumentType argumentType, String argument)
            throws InvalidArgumentException {
        return ArgumentParserManager.parse(argumentType, argument);
    }
}
