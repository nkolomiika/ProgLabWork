package org.example.utils.builders.avaliable;

import org.example.exceptions.file.FileReaderRuntimeException;
import org.example.exceptions.input.EmptyStringRuntimeException;
import org.example.exceptions.input.NegativeValueRuntimeException;
import org.example.model.data.Color;
import org.example.model.data.Country;
import org.example.model.data.Person;
import org.example.runners.RuntimeMode;
import org.example.utils.builders.abstracts.AbstractBuilder;
import org.example.utils.io.console.Console;

import java.io.IOException;

public class PersonBuilder extends AbstractBuilder {

    public static Person build() throws IOException {
        return new Person(
                inputName(),
                inputWeight(),
                inputEyeColor(),
                inputHairColor(),
                inputCountry()
        );
    }

    private static String inputName() throws IOException {
        String name;

        while (true) {
            try {
                Console.print("Input author name:");
                name = runtimeMode == RuntimeMode.CONSOLE
                        ? Console.nextLine() : fileReader.readLine();

                if (name.isEmpty()) throw new EmptyStringRuntimeException();
                break;
            } catch (RuntimeException exception) {
                Console.printError(exception.getMessage());
            }
        }
        return name;
    }

    private static Float inputWeight() throws IOException {
        String strWeight;
        float weight;

        while (true) {
            try {
                Console.print("Input author weight:");
                strWeight = runtimeMode == RuntimeMode.CONSOLE
                        ? Console.nextLine() : fileReader.readLine();

                if (strWeight.isEmpty()) return null;
                weight = Float.parseFloat(strWeight);
                if (weight <= 0) throw new NegativeValueRuntimeException();
                break;
            } catch (RuntimeException exception) {
                Console.printError(exception.getMessage());
            }
        }
        return weight;
    }

    private static Color inputEyeColor() throws IOException {
        String strEyeColor;
        Color eyeColor;

        while (true) {
            try {
                Console.print("Choose eye color from " + Color.getAllValues() + ":");
                strEyeColor = runtimeMode == RuntimeMode.CONSOLE
                        ? Console.nextLine() : fileReader.readLine();
                strEyeColor = strEyeColor.toUpperCase();

                if (strEyeColor.isEmpty()) throw new EmptyStringRuntimeException();
                eyeColor = Color.valueOf(strEyeColor);
                break;
            } catch (RuntimeException exception) {
                Console.printError(exception.getMessage());
            }
        }
        return eyeColor;
    }

    private static Color inputHairColor() throws IOException {
        String strHairColor;
        Color hairColor;

        while (true) {
            try {
                Console.print("Choose hair color from " + Color.getAllValues() + ":");
                strHairColor = runtimeMode == RuntimeMode.CONSOLE
                        ? Console.nextLine() : fileReader.readLine();
                strHairColor = strHairColor.toUpperCase();

                if (strHairColor.isEmpty()) return null;
                hairColor = Color.valueOf(strHairColor);
                break;
            } catch (RuntimeException exception) {
                Console.printError(exception.getMessage());
            }
        }
        return hairColor;
    }

    private static Country inputCountry() throws IOException {
        String strCountry;
        Country country;

        while (true) {
            try {
                Console.print("Choose country from " + Country.getAllValues() + ":");
                strCountry = runtimeMode == RuntimeMode.CONSOLE
                        ? Console.nextLine() : fileReader.readLine();
                strCountry = strCountry.toUpperCase();

                if (strCountry.isEmpty()) throw new EmptyStringRuntimeException();
                country = Country.valueOf(strCountry);
                break;
            } catch (RuntimeException exception) {
                Console.printError(exception.getMessage());
            }
        }
        return country;
    }
}
