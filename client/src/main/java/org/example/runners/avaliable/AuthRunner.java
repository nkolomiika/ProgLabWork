package org.example.runners.avaliable;

import lombok.SneakyThrows;
import org.example.exceptions.process.ExitObligedRuntimeException;
import org.example.network.ClientTCP;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.dto.User;
import org.example.network.model.ArgumentType;
import org.example.network.model.RuntimeMode;
import org.example.network.model.Status;
import org.example.runners.Pair;
import org.example.runners.abstracts.Runner;
import org.example.utils.builders.avaliable.UserBuilder;
import org.example.utils.io.console.Console;

import java.util.NoSuchElementException;

import static org.example.network.InitialResponseSender.getAvailableCommands;
import static org.example.utils.parser.avaliable.command.CommandParser.parse;

public class AuthRunner extends Runner {

    {
        runtimeMode = RuntimeMode.AUTH;
    }

    @Override
    @SneakyThrows
    public Pair run(User user) throws NoSuchElementException {
        availableCommands = getAvailableCommands();

        while (true) {
            try {
                String input = Console.nextLine();

                if (input.isEmpty()) continue;

                Request request = parse(input);
                String command = request.getCommand();
                String argument = request.getArgument();

                if (isInputValid(command, argument)) {
                    ArgumentType argumentType = availableCommands.get(command);
                    if (argumentType == ArgumentType.USER) {
                        user = UserBuilder.build();
                        request.setUser(user);
                    }

                    ClientTCP.sendRequest(request);
                    Response response = ClientTCP.receiveResponse();

                    Console.println(response.getData());

                    if (response.getStatus() == Status.LOGIN_SUCCESS)
                        return new Pair(RuntimeMode.CONSOLE, user);
                }

            } catch (NoSuchElementException exception) {
                throw new NoSuchElementException(exception.getMessage());
            } catch (ExitObligedRuntimeException exception) {
                Console.println(exception.getMessage());
                return new Pair(exception.getRuntimeMode(), UserBuilder.buildEmptyUser());
            } catch (RuntimeException exception) {
                Console.printError(exception.getMessage());
            }
        }
    }
}
