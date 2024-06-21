package org.example.utils.builders;

import org.example.exceptions.input.EmptyStringRuntimeException;
import org.example.exceptions.input.NegativeValueRuntimeException;
import org.example.model.Color;
import org.example.model.Country;
import org.example.model.Person;
import org.example.utils.io.Console;

public class PersonBuilder {
    public static Person build() {
        return new Person(
                inputName(),
                inputWeight(),
                inputEyeColor(),
                inputHairColor(),
                inputCountry()
        );
    }

    private static String inputName() {
        String name;

        while (true) {
            try {
                Console.print("Input author name:");
                name = Console.nextLine();

                if (name.isEmpty()) throw new EmptyStringRuntimeException();
                break;
            } catch (RuntimeException exception) {
                Console.printError(exception.getMessage());
            }
        }
        return name;
    }

    private static Float inputWeight() {
        String strWeight;
        float weight;

        while (true) {
            try {
                Console.print("Input author weight:");
                strWeight = Console.nextLine();
                
                weight = Float.parseFloat(strWeight);
                if (strWeight.isEmpty()) return null;
                if (weight <= 0) throw new NegativeValueRuntimeException();
                break;
            } catch (RuntimeException exception) {
                Console.printError(exception.getMessage());
            }
        }
        return weight;
    }

    private static Color inputEyeColor() {
        String strEyeColor;
        Color eyeColor;

        while (true) {
            try {
                Console.print("Choose eye color from " + Color.getAllValues() + ":");
                strEyeColor = Console.nextLine().toUpperCase();
                
                if (strEyeColor.isEmpty()) throw new EmptyStringRuntimeException();
                eyeColor = Color.valueOf(strEyeColor);
                break;
            } catch (RuntimeException exception) {
                Console.printError(exception.getMessage());
            }
        }
        return eyeColor;
    }

    private static Color inputHairColor() {
        String strHairColor;
        Color hairColor;

        while (true) {
            try {
                Console.print("Choose hair color from " + Color.getAllValues() + ":");
                strHairColor = Console.nextLine().toUpperCase();

                if (strHairColor.isEmpty()) return null;
                hairColor = Color.valueOf(strHairColor);
                break;
            } catch (RuntimeException exception) {
                Console.printError(exception.getMessage());
            }
        }
        return hairColor;
    }

    private static Country inputCountry() {
        String strCountry;
        Country country;

        while (true) {
            try {
                Console.print("Choose country from " + Country.getAllValues() + ":");
                strCountry = Console.nextLine().toUpperCase();

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
