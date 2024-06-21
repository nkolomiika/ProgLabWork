package org.example.commands.avaliable;

import org.example.commands.abstarct.Command;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.model.Status;

public final class MeowCommand extends Command {
    public MeowCommand() {
        super("meow", "СЕКРЕТ-СЕКРЕТ-СЕКРЕТ");
    }

    @Override
    public Response execute(Request request) {
        return new Response(Status.OK, "Meow! Meow!");
    }
}
