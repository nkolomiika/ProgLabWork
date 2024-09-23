package org.example.utils.builders.avaliable;

import org.example.exceptions.input.EmptyStringRuntimeException;
import org.example.network.dto.User;
import org.example.utils.builders.abstracts.AbstractBuilder;
import org.example.utils.io.console.Console;

import java.io.IOException;

public class UserBuilder extends AbstractBuilder {


    public static User build() throws IOException {
        Console.setOutputSymbol("");
        User user = new User(
                inputUsername(),
                inputPassword()
        );
        Console.setOutputSymbol(">");
        return user;
    }

    public static User buildEmptyUser() {
        return new User();
    }

    private static String inputUsername() throws IOException {
        String username;

        while (true) {
            try {
                Console.print("Input your username:");
                username = Console.nextLine();

                if (username.isEmpty()) throw new EmptyStringRuntimeException();
                break;
            } catch (RuntimeException exception) {
                Console.printError(exception.getMessage());
            }
        }
        return username;
    }

    private static String inputPassword() throws IOException {
        String password;

        while (true) {
            try {
                Console.print("Input your password:");
                password = Console.nextLine();

                if (password.isEmpty()) throw new EmptyStringRuntimeException();
                break;
            } catch (RuntimeException exception) {
                Console.printError(exception.getMessage());
            }
        }
        return password;
    }
}
