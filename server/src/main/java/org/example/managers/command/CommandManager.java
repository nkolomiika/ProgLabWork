package org.example.managers.command;

import lombok.Getter;
import org.example.commands.abstarct.Command;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.dto.User;
import org.example.network.model.ArgumentType;
import org.example.network.model.Role;
import org.example.network.model.RuntimeMode;

import java.sql.SQLException;
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

    public Map<String, ArgumentType> getAllCommandsName(Role role) {
        return commands.entrySet().stream()
                .filter(entry -> entry.getValue().getRole().equals(role) ||
                        entry.getValue().getRole().equals(Role.ALL))
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        entry -> entry.getValue().getArgumentType()
                ));
    }

    public String getAllCommandsNameWithDescription(User user) {
        return commands.values().stream()
                .filter(cmd -> {
                    if (user != null)
                        return cmd.getRole() == Role.ALL || cmd.getRole() == Role.AUTH;
                    else
                        return cmd.getRole() == Role.ALL || cmd.getRole() == Role.NON_AUTH;
                })
                .map(cmd -> cmd.getName() + " : " + cmd.getDescription())
                .collect(Collectors.joining("\n"));
    }


    public void addAllCommands(List<Command> commandList) {
        commandList.forEach(this::addCommand);
    }

    public void addCommand(Command command) {
        commands.put(command.getName(), command);
    }

    public Response executeCommand(Request request) throws SQLException {
        return commands
                .get(request.getCommand())
                .execute(request);
    }


}