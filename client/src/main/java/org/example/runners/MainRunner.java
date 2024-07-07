package org.example.runners;

import lombok.SneakyThrows;
import org.example.exceptions.collection.InvalidArgumentException;
import org.example.exceptions.file.FindRecursionRuntimeException;
import org.example.exceptions.process.CommandNotFoundException;
import org.example.exceptions.process.ExitObligedRuntimeException;
import org.example.managers.ArgumentParserManager;
import org.example.model.data.IdCounter;
import org.example.network.ClientTCP;
import org.example.network.InitialResponseSender;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.model.ArgumentType;
import org.example.network.model.Status;
import org.example.runner.Runner;
import org.example.utils.builders.avaliable.LabWorkBuilder;
import org.example.utils.io.console.Console;
import org.example.utils.io.file.CustomFileReader;
import org.example.utils.parser.avaliable.argument.*;

import java.io.EOFException;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.example.network.InitialResponseSender.getAvailableCommands;
import static org.example.utils.parser.avaliable.command.CommandParser.parse;

public class MainRunner extends Runner {
    private static RuntimeMode runtimeMode;
    private final CustomFileReader customFileReader;
    private static Map<String, ArgumentType> availableCommands;
    private final ArgumentParserManager argumentParserManager;

    static {
        runtimeMode = RuntimeMode.CONSOLE;
    }

    {
        this.customFileReader = new CustomFileReader();
        this.argumentParserManager = new ArgumentParserManager();
    }

    @Override
    @SneakyThrows
    public void run() {
        availableCommands = getAvailableCommands();
        InitialResponseSender.setNexIdCounter();

        while (true) {
            try {
                String input = runtimeMode == RuntimeMode.CONSOLE
                        ? Console.nextLine()
                        : customFileReader.readLine();
                LabWorkBuilder.setRuntimeMode(runtimeMode);

                if (input.isEmpty()) continue;

                Request request = parse(input);
                String command = request.getCommand();
                String argument = request.getArgument();

                if (isInputValid(command, argument)) {
                    if (!recursionCheck(command)) {

                        ArgumentType argumentType = availableCommands.get(command);
                        if (argumentType == ArgumentType.LAB_WORK
                                || argumentType == ArgumentType.NUMBER_AND_LAB_WORK)
                            request.setLabWork(LabWorkBuilder.build());

                        ClientTCP.sendRequest(request);
                        Response response = ClientTCP.receiveResponse();

                        if (response.status() == Status.ERROR)
                            throw new RuntimeException(response.data());
                        else if (response.status() == Status.EXIT)
                            throw new ExitObligedRuntimeException(response.data());
                        else if (response.status() == Status.EXECUTE_SCRIPT) {
                            runtimeMode = RuntimeMode.FILE;
                            customFileReader.setFile(argument);
                            LabWorkBuilder.setFileReader(customFileReader);
                            LabWorkBuilder.setRuntimeMode(runtimeMode);
                        }
                        Console.println(response.data());
                    }
                }

            } catch (ExitObligedRuntimeException | NoSuchElementException exception) {
                Console.println(exception.getMessage());
                break;
            } catch (EOFException exception) {
                Console.println(exception.getMessage());
                runtimeMode = RuntimeMode.CONSOLE;
                LabWorkBuilder.setRuntimeMode(runtimeMode);
            } catch (RuntimeException | IOException exception) {
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

    private boolean recursionCheck(String command) {
        return runtimeMode == RuntimeMode.FILE
                && command.equals("execute_script");
    }

}
