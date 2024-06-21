package org.example.utils.builders;

import org.example.exceptions.input.EmptyStringRuntimeException;
import org.example.exceptions.input.OutOfBoundsRuntimeException;
import org.example.model.Coordinates;
import org.example.utils.io.Console;

public class CoordinatesBuilder {
    public static Coordinates build() {
        return new Coordinates(
                inputX(),
                inputY()
        );
    }

    private static Integer inputX() {
        String strX;
        int x, maxValue = 583;

        while (true) {
            try {
                Console.print("Input x in coordinates:");
                strX = Console.nextLine();

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

    private static float inputY() {
        String strY;
        float y;

        while (true) {
            try {
                Console.print("Input y in coordinates:");
                strY = Console.nextLine();

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

