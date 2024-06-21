package org.example.utils.builders;

import org.example.exceptions.input.EmptyStringRuntimeException;
import org.example.exceptions.input.NegativeValueRuntimeException;
import org.example.model.Difficulty;
import org.example.model.LabWork;
import org.example.utils.io.Console;

public class LabWorkBuilder {

    /*
    * private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Integer minimalPoint; //Поле может быть null, Значение поля должно быть больше 0
    private Difficulty difficulty; //Поле не может быть null
    private Person author; //Поле не может быть null*/

    public static LabWork build() {
        Console.setOutputSymbol("");
        LabWork lab = new LabWork(
                inputName(),
                CoordinatesBuilder.build(),
                inputMinimalPoint(),
                inputDifficulty(),
                PersonBuilder.build()
        );
        Console.setOutputSymbol(">");
        return lab;
    }

    private static String inputName() {
        String name;

        while (true) {
            try {
                Console.print("Input lab work name:");
                name = Console.nextLine();

                if (name.isEmpty()) throw new EmptyStringRuntimeException();
                break;
            } catch (RuntimeException exception) {
                Console.printError(exception.getMessage());
            }
        }
        return name;
    }

    private static Integer inputMinimalPoint() {
        String strMinimalPoint;
        int minimalPoint;

        while (true) {
            try {
                Console.print("Input minimal point:");
                strMinimalPoint = Console.nextLine();

                if (strMinimalPoint.isEmpty()) return null;
                minimalPoint = Integer.parseInt(strMinimalPoint);
                if (minimalPoint <= 0) throw new NegativeValueRuntimeException();
                break;
            } catch (RuntimeException exception) {
                Console.printError(exception.getMessage());
            }
        }
        return minimalPoint;
    }

    private static Difficulty inputDifficulty() {
        String strDifficulty;
        Difficulty difficulty;

        while (true) {
            try {
                Console.print("Choose lab work difficulty from " + Difficulty.getAllValues() + ":");
                strDifficulty = Console.nextLine().toUpperCase();

                difficulty = Difficulty.valueOf(strDifficulty);
                break;
            } catch (RuntimeException exception) {
                Console.printError(exception.getMessage());
            }
        }
        return difficulty;
    }

}
