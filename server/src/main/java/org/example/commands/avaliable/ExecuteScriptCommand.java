package org.example.commands.avaliable;

import org.example.commands.abstarct.Command;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.model.ArgumentType;
import org.example.network.model.Status;

public class ExecuteScriptCommand extends Command {

    public ExecuteScriptCommand() {
        super("execute_script", "запустить скрипт с командами", ArgumentType.STRING);
    }

    @Override
    public Response execute(Request request) {
        return new Response(Status.EXECUTE_SCRIPT, "script started execution!");
    }
}
