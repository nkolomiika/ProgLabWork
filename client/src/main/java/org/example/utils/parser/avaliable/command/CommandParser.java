package org.example.utils.parser.avaliable.command;

import org.example.exceptions.input.InvalidInputFormatException;
import org.example.network.ClientTCP;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.dto.User;
import org.example.network.model.ArgumentType;
import org.example.parsers.Parser;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommandParser implements Parser {

    public static Request parse(String input) throws InvalidInputFormatException {
        Pattern inputPattern = Pattern.compile("^\\w+\\s*(?:\\d+|\\S+)?$");
        Matcher inputMatcher = inputPattern.matcher(input);

        if (!inputMatcher.find()) throw new InvalidInputFormatException();
        List<String> command = List.of(input.split(" "));

        if (command.size() == 1) return new Request(command.get(0));
        return new Request(command.get(0), command.get(command.size() - 1));
    }

    public static Map.Entry<String, ArgumentType> parseCommand(String command) {
        String[] parts = command.split("=");
        String commandName = parts[0];
        ArgumentType argumentType = ArgumentType.valueOf(parts[1]);
        return Map.entry(commandName, argumentType);
    }
}
