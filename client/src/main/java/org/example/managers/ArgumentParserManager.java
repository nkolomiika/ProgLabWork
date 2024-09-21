package org.example.managers;

import org.example.network.model.ArgumentType;
import org.example.utils.parser.avaliable.argument.UserArgumentParser;
import org.example.utils.parser.abstracts.argument.ArgumentParser;
import org.example.utils.parser.avaliable.argument.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ArgumentParserManager {

    private static final Map<ArgumentType, ArgumentParser> parsers;

    static {
        parsers = new HashMap<>();
        addAllParsers(
                List.of(
                        new NoneArgumentParser(),
                        new NumberArgumentParser(),
                        new EnumDifficultyArgumentParser(),
                        new StringArgumentParser(),
                        new NumberAndLabWorkArgumentParser(),
                        new LabWorkArgumentParser(),
                        new UserArgumentParser()
                )
        );
    }

    public static void addParser(ArgumentParser argumentParser) {
        parsers.put(argumentParser.getArgumentType(), argumentParser);
    }

    public static void addAllParsers(List<ArgumentParser> parserList) {
        parserList.forEach(ArgumentParserManager::addParser);
    }

    public static boolean parse(ArgumentType argumentType, String argument) {
        return parsers.get(argumentType).parse(argument);
    }

}
