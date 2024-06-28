package org.example.utils.builders.avaliable;

import org.example.exceptions.file.FileReaderRuntimeException;
import org.example.exceptions.input.EmptyStringRuntimeException;
import org.example.exceptions.input.OutOfBoundsRuntimeException;
import org.example.model.data.Coordinates;
import org.example.runners.RuntimeMode;
import org.example.utils.builders.abstracts.AbstractBuilder;
import org.example.utils.io.console.Console;

import java.io.IOException;

public class CoordinatesBuilder extends AbstractBuilder {
    public static Coordinates build() throws IOException {
        return new Coordinates(
                inputX(),
                inputY()
        );
    }

    private static Integer inputX() throws IOException {
        String strX;
        int x, maxValue = 583;

        while (true) {
            try {
                Console.print("Input x in coordinates:");
                strX = runtimeMode == RuntimeMode.CONSOLE
                        ? Console.nextLine() : fileReader.readLine();

                if (strX.isEmpty()) return null;
                x = Integer.parseInt(strX);
                if (x > maxValue) throw new OutOfBoundsRuntimeException(maxValue);
                break;
            } catch (RuntimeException exception) {
                Console.printError(exception.getMessage());
            }
        }
        return x;
    }

    private static float inputY() throws IOException {
        String strY;
        float y;

        while (true) {
            try {
                Console.print("Input y in coordinates:");
                strY = runtimeMode == RuntimeMode.CONSOLE
                        ? Console.nextLine() : fileReader.readLine();

                if (strY.isEmpty()) throw new EmptyStringRuntimeException();
                y = Float.parseFloat(strY);
                break;
            } catch (RuntimeException exception) {
                Console.printError(exception.getMessage());
            }
        }
        return y;
    }
}

