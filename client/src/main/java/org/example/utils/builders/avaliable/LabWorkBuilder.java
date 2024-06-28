package org.example.utils.builders.avaliable;

import org.example.exceptions.file.FileReaderRuntimeException;
import org.example.exceptions.input.EmptyStringRuntimeException;
import org.example.exceptions.input.NegativeValueRuntimeException;
import org.example.model.data.Difficulty;
import org.example.model.data.LabWork;
import org.example.runners.RuntimeMode;
import org.example.utils.builders.abstracts.AbstractBuilder;
import org.example.utils.io.console.Console;
import org.example.utils.io.file.CustomFileReader;

import java.io.IOException;

public class LabWorkBuilder extends AbstractBuilder {

    public static void setFileReader(CustomFileReader fileReader) {
        LabWorkBuilder.fileReader = fileReader;
        PersonBuilder.setFileReader(fileReader);
        CoordinatesBuilder.setFileReader(fileReader);
    }

    public static void setRuntimeMode(RuntimeMode runtimeMode) {
        LabWorkBuilder.runtimeMode = runtimeMode;
        PersonBuilder.setRuntimeMode(runtimeMode);
        CoordinatesBuilder.setRuntimeMode(runtimeMode);
    }

    /*
    * private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически
    private String name; //Поле не может быть null, Строка не может быть пустой
    private Coordinates coordinates; //Поле не может быть null
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически
    private Integer minimalPoint; //Поле может быть null, Значение поля должно быть больше 0
    private Difficulty difficulty; //Поле не может быть null
    private Person author; //Поле не может быть null
    */

    public static LabWork build() throws FileReaderRuntimeException {
        try {
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
        } catch (IOException exception) {
            Console.setOutputSymbol(">");
            throw new FileReaderRuntimeException();
        }
    }

    private static String inputName() throws FileReaderRuntimeException {
        String name;

        while (true) {
            try {
                Console.print("Input lab work name:");
                name = runtimeMode == RuntimeMode.CONSOLE
                        ? Console.nextLine() : fileReader.readLine();

                if (name.isEmpty()) throw new EmptyStringRuntimeException();
                break;
            } catch (RuntimeException exception) {
                Console.printError(exception.getMessage());
            } catch (IOException e) {
                throw new FileReaderRuntimeException();
            }
        }
        return name;
    }

    private static Integer inputMinimalPoint() throws FileReaderRuntimeException {
        String strMinimalPoint;
        int minimalPoint;

        while (true) {
            try {
                Console.print("Input minimal point:");
                strMinimalPoint = runtimeMode == RuntimeMode.CONSOLE
                        ? Console.nextLine() : fileReader.readLine();

                if (strMinimalPoint.isEmpty()) return null;
                minimalPoint = Integer.parseInt(strMinimalPoint);
                if (minimalPoint <= 0) throw new NegativeValueRuntimeException();
                break;
            } catch (RuntimeException exception) {
                Console.printError(exception.getMessage());
            } catch (IOException e) {
                throw new FileReaderRuntimeException();
            }
        }
        return minimalPoint;
    }

    private static Difficulty inputDifficulty() throws FileReaderRuntimeException {
        String strDifficulty;
        Difficulty difficulty;

        while (true) {
            try {
                Console.print("Choose lab work difficulty from " + Difficulty.getAllValues() + ":");
                strDifficulty = runtimeMode == RuntimeMode.CONSOLE
                        ? Console.nextLine() : fileReader.readLine();
                strDifficulty = strDifficulty.toUpperCase();

                difficulty = Difficulty.valueOf(strDifficulty);
                break;
            } catch (RuntimeException exception) {
                Console.printError(exception.getMessage());
            } catch (IOException e) {
                throw new FileReaderRuntimeException();
            }
        }
        return difficulty;
    }

}
