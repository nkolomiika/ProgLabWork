package org.example.runners.abstracts;

import lombok.Getter;
import org.example.network.dto.User;
import org.example.network.model.ArgumentType;
import org.example.network.model.RuntimeMode;

import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

public abstract class Runner {
    @Getter
    protected RuntimeMode runtimeMode;
    protected Map<String, ArgumentType> availableCommands;
    public abstract RuntimeMode run(User user) throws IOException, NoSuchElementException;
}
