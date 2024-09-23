package org.example.commands.avaliable.auth;

import org.example.commands.abstarct.Command;
import org.example.managers.db.DatabaseManager;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.model.ArgumentType;
import org.example.network.model.Role;
import org.example.network.model.Status;

public class RegisterCommand extends Command {
    public RegisterCommand() {
        super(
                "register",
                "зарегистрировать новый аккаунт",
                ArgumentType.USER,
                Role.NON_AUTH
        );
    }

    @Override
    public Response execute(Request request) {
        if (DatabaseManager.addUser(request.getUser()))
            return new Response(
                    Status.LOGIN_SUCCESS,
                    "User has successfully registered!\nWelcome, " + request.getUser().getUsername() + "!"
            );
        return new Response(Status.LOGIN_FAILED, "Ooops! Cannot register this user :(");
    }
}
