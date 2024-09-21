package org.example.runners.avaliable;

import lombok.SneakyThrows;
import org.example.exceptions.process.ExitObligedRuntimeException;
import org.example.network.dto.Request;
import org.example.network.dto.User;
import org.example.network.model.RuntimeMode;
import org.example.runners.abstracts.Runner;
import org.example.utils.io.console.Console;

import java.util.NoSuchElementException;

import static org.example.utils.parser.avaliable.command.CommandParser.parse;

public class AuthRunner extends Runner {

    {
        runtimeMode = RuntimeMode.AUTH;
    }

    @SneakyThrows
    @Override
    public RuntimeMode run(User user) throws NoSuchElementException {
        while (true) {
            try {

                String input = Console.nextLine();

                if (input.isEmpty()) continue;

                Request request = parse(input);
                String command = request.getCommand();
                String argument = request.getArgument();

            } catch (NoSuchElementException exception) {
                throw new NoSuchElementException(exception.getMessage());
            } catch (ExitObligedRuntimeException exception) {
                Console.println(exception.getMessage());
                return exception.getRuntimeMode();
            } catch (RuntimeException exception) {
                Console.printError(exception.getMessage());
            }
        }
    }
}
