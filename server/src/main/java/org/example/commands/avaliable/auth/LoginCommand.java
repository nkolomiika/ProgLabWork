package org.example.commands.avaliable.auth;

import org.example.commands.abstarct.Command;
import org.example.managers.db.DatabaseManager;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.dto.User;
import org.example.network.model.ArgumentType;
import org.example.network.model.Role;
import org.example.network.model.Status;

public class LoginCommand extends Command {

    public LoginCommand() {
        super(
                "login",
                "войти в аккаунт",
                ArgumentType.USER,
                Role.NON_AUTH
        );
    }

    @Override
    public Response execute(Request request) {
        User user = request.getUser();
        if (DatabaseManager.confirmUser(user))
            return new Response(Status.LOGIN_SUCCESS, "Welcome back, " + user.getUsername() + "!");
        return new Response(Status.LOGIN_FAILED, "Ooops! Wrong credentials, cannot login :(");
    }
}
