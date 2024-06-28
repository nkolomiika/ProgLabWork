package org.example.utils.io.console;

import lombok.Setter;

import java.util.Scanner;

public class Console {
    @Setter
    private static String outputSymbol;
    private static final Scanner userScanner;

    static {
        outputSymbol = ">";
        userScanner = new Scanner(System.in);
    }

    public static String nextLine() {
        System.out.print(outputSymbol + " ");
        return userScanner.nextLine().trim();
    }

    public static void println(Object o) {
        System.out.println(o);
    }

    public static void print(Object o) {
        System.out.print(o);
    }

    public static void printError(Object o) {
        println(ConsoleColors.toColor(o.toString(), ConsoleColors.RED));
    }
}
