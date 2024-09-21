package org.example.network;

import org.example.network.dto.Response;
import org.example.network.model.ArgumentType;
import org.example.utils.parser.avaliable.command.CommandParser;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class InitialResponseSender {
    public static Map<String, ArgumentType> getAvailableCommands()
            throws IOException, ClassNotFoundException {
        Response initResponse = ClientTCP.receiveResponse();

        String initData = initResponse.getData();
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
}
