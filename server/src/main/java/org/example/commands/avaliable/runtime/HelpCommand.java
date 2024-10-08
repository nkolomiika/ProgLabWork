package org.example.commands.avaliable.runtime;

import org.example.commands.abstarct.Command;
import org.example.managers.command.CommandManager;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.model.Role;
import org.example.network.model.Status;

public final class HelpCommand extends Command {

    private final CommandManager commandManager;

    public HelpCommand(CommandManager commandManager) {
        super("help", "вывести справку по доступным командам", Role.ALL);
        this.commandManager = commandManager;
    }

    @Override
    public Response execute(Request request) {
        return new Response(
                Status.OK,
                commandManager.getAllCommandsNameWithDescription(request.getUser())
        );
    }
}
