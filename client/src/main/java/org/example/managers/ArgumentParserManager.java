package org.example.managers;

import org.example.network.model.ArgumentType;
import org.example.utils.parser.abstracts.argument.ArgumentParser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ArgumentParserManager {

    private final Map<ArgumentType, ArgumentParser> parsers;

    {
        this.parsers = new HashMap<>();
    }

    public void addParser(ArgumentParser argumentParser) {
        this.parsers.put(argumentParser.getArgumentType(), argumentParser);
    }

    public void addAllParsers(List<ArgumentParser> parserList) {
        parserList.forEach(this::addParser);
    }

    public boolean parse(ArgumentType argumentType, String argument) {
        return this.parsers.get(argumentType).parse(argument);
    }

}
