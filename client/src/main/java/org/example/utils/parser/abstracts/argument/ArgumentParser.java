package org.example.utils.parser.abstracts.argument;

import lombok.Getter;
import org.example.exceptions.collection.InvalidArgumentException;
import org.example.network.model.ArgumentType;
import org.example.parsers.Parser;

@Getter
public abstract class ArgumentParser implements Parser {
    private final ArgumentType argumentType;

    public ArgumentParser(ArgumentType argumentType) {
        this.argumentType = argumentType;
    }

    public abstract boolean parse(String argument) throws InvalidArgumentException;
}
