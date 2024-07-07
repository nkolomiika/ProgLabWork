package org.example.utils.parser.avaliable.argument;

import org.example.exceptions.collection.InvalidArgumentException;
import org.example.network.model.ArgumentType;
import org.example.utils.parser.abstracts.argument.ArgumentParser;

public class NumberAndLabWorkArgumentParser extends ArgumentParser {

    public NumberAndLabWorkArgumentParser() {
        super(ArgumentType.NUMBER_AND_LAB_WORK);
    }

    @Override
    public boolean parse(String argument) throws InvalidArgumentException {
        try {
            Double number = Double.parseDouble(argument);
            return true;
        } catch (NumberFormatException exception) {
            throw new InvalidArgumentException("Ooops! Cannot convert argument=" + argument + " to number :(");
        }
    }
}
