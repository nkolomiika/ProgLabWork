package org.example.runners.avaliable;

import lombok.SneakyThrows;
import org.example.exceptions.process.ExitObligedRuntimeException;
import org.example.network.ClientTCP;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.dto.User;
import org.example.network.model.ArgumentType;
import org.example.network.model.Status;
import org.example.network.model.RuntimeMode;
import org.example.runners.Pair;
import org.example.runners.abstracts.Runner;
import org.example.utils.builders.avaliable.LabWorkBuilder;
import org.example.utils.builders.avaliable.UserBuilder;
import org.example.utils.io.console.Console;
import org.example.utils.io.file.CustomFileReader;

import java.io.EOFException;
import java.io.IOException;
import java.util.Map;
import java.util.NoSuchElementException;

import static org.example.network.InitialResponseSender.getAvailableCommands;
import static org.example.utils.parser.avaliable.command.CommandParser.parse;

public class MainRunner extends Runner {
    private final CustomFileReader customFileReader;

    {
        runtimeMode = RuntimeMode.CONSOLE;
        this.customFileReader = new CustomFileReader();
    }

    @Override
    @SneakyThrows
    public Pair run(User user) throws NoSuchElementException {
        availableCommands = getAvailableCommands();

        while (true) {
            try {

                String input =
                        runtimeMode == RuntimeMode.CONSOLE
                        ? Console.nextLine() : customFileReader.readLine();
                LabWorkBuilder.setRuntimeMode(runtimeMode);

                if (input.isEmpty()) continue;

                Request request = parse(input);
                String command = request.getCommand();
                String argument = request.getArgument();

                if (isInputValid(command, argument)) {
                    if (!recursionCheck(command)) {

                        ArgumentType argumentType = availableCommands.get(command);
                        request.setUser(user);

                        if (argumentType == ArgumentType.LAB_WORK
                                || argumentType == ArgumentType.NUMBER_AND_LAB_WORK)
                            request.setLabWork(LabWorkBuilder.build());

                        ClientTCP.sendRequest(request);
                        Response response = ClientTCP.receiveResponse();

                        if (response.getStatus() == Status.ERROR)
                            throw new RuntimeException(response.getData());
                        else if (response.getStatus() == Status.EXIT)
                            throw new ExitObligedRuntimeException(
                                    response.getData(),
                                    response.getRuntimeMode()
                            );
                        else if (response.getStatus() == Status.EXECUTE_SCRIPT) {
                            runtimeMode = RuntimeMode.FILE;
                            customFileReader.setFile(argument);
                            LabWorkBuilder.setFileReader(customFileReader);
                        }
                        Console.println(response.getData());
                    }
                }
            } catch (NoSuchElementException exception) {
                throw new NoSuchElementException(exception.getMessage());
            } catch (ExitObligedRuntimeException exception) {
                Console.println(exception.getMessage());
                return new Pair(exception.getRuntimeMode(), UserBuilder.buildEmptyUser());
            } catch (EOFException exception) {
                Console.println(exception.getMessage());
                runtimeMode = RuntimeMode.CONSOLE;
                LabWorkBuilder.setRuntimeMode(runtimeMode);
            } catch (RuntimeException
                     | IOException exception) {
                Console.printError(exception.getMessage());
            }
        }
    }

    private boolean recursionCheck(String command) {
        return runtimeMode == RuntimeMode.FILE
                && command.equals("execute_script");
    }

}
