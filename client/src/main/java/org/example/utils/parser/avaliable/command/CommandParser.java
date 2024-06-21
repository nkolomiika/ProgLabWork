package org.example.utils.parser.avaliable.command;

import org.example.exceptions.input.InvalidInputFormatException;
import org.example.network.ClientTCP;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
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

    public static Map<String, ArgumentType> getAvailableCommands() throws IOException, ClassNotFoundException {
        Response initResponse = ClientTCP.receiveResponse();

        String initData = initResponse.data();
        initData = initData.substring(1, initData.length() - 1);

        List<String> listOfNames = Arrays.asList(initData.split(", "));
        return listOfNames.stream()
                .map(CommandParser::parseCommand)   // Преобразуем каждую строку в Map.Entry
                .collect(Collectors.toMap(
                        Map.Entry::getKey,          // Ключ - имя команды
                        Map.Entry::getValue,        // Значение - тип аргумента
                        (e1, e2) -> e1,             // Если встречаются одинаковые ключи, оставляем первый
                        HashMap::new                // Создаем новую HashMap
                ));
    }

    private static Map.Entry<String, ArgumentType> parseCommand(String command) {
        String[] parts = command.split("=");
        String commandName = parts[0];
        ArgumentType argumentType = ArgumentType.valueOf(parts[1]);
        return Map.entry(commandName, argumentType);
    }
}
