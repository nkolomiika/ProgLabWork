package org.example.runners;

import lombok.SneakyThrows;
import org.example.exceptions.collection.InvalidArgumentException;
import org.example.exceptions.process.CommandNotFoundException;
import org.example.exceptions.process.ExitObligedRuntimeException;
import org.example.managers.ArgumentParserManager;
import org.example.model.LabWork;
import org.example.network.ClientTCP;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.model.ArgumentType;
import org.example.network.model.Status;
import org.example.runner.Runner;
import org.example.utils.builders.LabWorkBuilder;
import org.example.utils.io.Console;
import org.example.utils.parser.avaliable.argument.EnumDifficultyArgumentParser;
import org.example.utils.parser.avaliable.argument.NoneArgumentParser;
import org.example.utils.parser.avaliable.argument.NumberArgumentParser;
import org.example.utils.parser.avaliable.argument.StringArgumentParser;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.example.utils.parser.avaliable.command.CommandParser.getAvailableCommands;
import static org.example.utils.parser.avaliable.command.CommandParser.parse;

public class MainRunner extends Runner {

    private static Map<String, ArgumentType> availableCommands;
    private final ArgumentParserManager argumentParserManager;

    {
        this.argumentParserManager = new ArgumentParserManager();
        this.argumentParserManager.addAllParsers(
                List.of(
                        new NoneArgumentParser(),
                        new NumberArgumentParser(),
                        new EnumDifficultyArgumentParser(),
                        new StringArgumentParser()
                )
        );
    }

    @Override
    @SneakyThrows
    public void run() {
        availableCommands = getAvailableCommands();

        while (true) {
            try {
                String input = Console.nextLine();
                if (input.isEmpty()) continue;

                Request request = parse(input);
                String command = request.getCommand();
                String argument = request.getArgument();

                if (isInputValid(command, argument)) {

                    if (command.equals("add")
                            || command.equals("add_if_min"))
                        request.setLabWork(LabWorkBuilder.build());

                    ClientTCP.sendRequest(request);
                    Response response = ClientTCP.receiveResponse();

                    if (response.status() == Status.ERROR)
                        throw new RuntimeException(response.data());
                    else if (response.status() == Status.EXIT)
                        throw new ExitObligedRuntimeException(response.data());
                    else
                        Console.println(response.data());
                }

            } catch (ExitObligedRuntimeException | NoSuchElementException exception) {
                Console.println(exception.getMessage());
                break;
            } catch (RuntimeException exception) {
                Console.printError(exception.getMessage());
            }
        }
    }

    private boolean isInputValid(String command, String argument)
            throws RuntimeException {
        ArgumentType argumentType = availableCommands.get(command);
        if (argumentType == null) throw new CommandNotFoundException();
        if (!isArgumentValid(argumentType, argument)) throw new InvalidArgumentException();
        return true;
    }

    private boolean isArgumentValid(ArgumentType argumentType, String argument)
            throws InvalidArgumentException {
        return this.argumentParserManager.parse(argumentType, argument);
    }

}
