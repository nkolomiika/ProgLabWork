package org.example.utils.parser.avaliable.argument;

import org.example.exceptions.collection.InvalidArgumentException;
import org.example.network.model.ArgumentType;
import org.example.utils.parser.abstracts.argument.ArgumentParser;

import java.util.Objects;

public final class StringArgumentParser extends ArgumentParser {
    public StringArgumentParser() {
        super(ArgumentType.STRING);
    }

    @Override
    public boolean parse(String argument) throws InvalidArgumentException {
        return Objects.nonNull(argument);
    }
}
