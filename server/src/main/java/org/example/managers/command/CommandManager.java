package org.example.managers.command;

import lombok.Getter;
import org.example.commands.abstarct.Command;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.model.ArgumentType;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Getter
public class CommandManager {
    private final Map<String, Command> commands;

    {
        this.commands = new HashMap<>();
    }

    public Command getCommandByName(String name) {
        return commands.get(name);
    }

    public Map<String, ArgumentType> getAllCommandsName() {
        return commands.entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().getArgumentType()
                ));
    }

    public String getAllCommandsNameWithDescription() {
        return commands.values().stream()
                .map(cmd -> cmd.getName() + " : " + cmd.getDescription())
                .collect(Collectors.joining("\n"));
    }

    public void addAllCommands(List<Command> commandList) {
        commandList.forEach(this::addCommand);
    }

    public void addCommand(Command command) {
        commands.put(command.getName(), command);
    }

    public Response executeCommand(Request request) {
        return commands
                .get(request.getCommand())
                .execute(request);
    }


}