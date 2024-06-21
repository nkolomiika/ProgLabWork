package org.example.commands.avaliable;

import org.example.commands.abstarct.Command;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.model.Status;

public final class ExitCommand extends Command {

    public ExitCommand() {
        super("exit", "завершить программу, сохранив коллекцию в файл");
    }

    @Override
    public Response execute(Request request) {
        return new Response(Status.EXIT, "Bye bye!");
    }
}
