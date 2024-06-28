package org.example.utils.parser.avaliable.argument;

import org.example.exceptions.collection.InvalidArgumentException;
import org.example.model.data.Difficulty;
import org.example.network.model.ArgumentType;
import org.example.utils.parser.abstracts.argument.ArgumentParser;

public final class EnumDifficultyArgumentParser
        extends ArgumentParser {

    public EnumDifficultyArgumentParser() {
        super(ArgumentType.ENUM_DIFFICULTY);
    }

    @Override
    public boolean parse(String argument) throws InvalidArgumentException {
        try {
            var difficulty = Difficulty.valueOf(argument.toUpperCase());
            return true;
        } catch (IllegalArgumentException exception) {
            throw new InvalidArgumentException("Ooops! Cannot convert argument to Difficulty enum :(");
        }
    }
}
