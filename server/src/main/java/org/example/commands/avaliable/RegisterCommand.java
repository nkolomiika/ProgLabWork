package org.example.commands.avaliable;

import org.example.commands.abstarct.Command;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.model.ArgumentType;
import org.example.network.model.Role;

public class RegisterCommand extends Command {
    public RegisterCommand() {
        super("register", "зарегистрироваться", ArgumentType.USER, Role.NON_AUTH);
    }

    @Override
    public Response execute(Request request) {
        return null;
    }
}
