package org.example.managers;

import org.example.network.model.ArgumentType;
import org.example.utils.parser.abstracts.argument.ArgumentParser;
import org.example.utils.parser.avaliable.argument.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public final class ArgumentParserManager {

    private final Map<ArgumentType, ArgumentParser> parsers;

    {
        this.parsers = new HashMap<>();
        this.addAllParsers(
                List.of(
                        new NoneArgumentParser(),
                        new NumberArgumentParser(),
                        new EnumDifficultyArgumentParser(),
                        new StringArgumentParser(),
                        new NumberAndLabWorkArgumentParser(),
                        new LabWorkArgumentParser()
                )
        );
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
