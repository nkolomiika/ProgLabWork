package org.example.utils.builders.avaliable;

import org.example.exceptions.input.EmptyStringRuntimeException;
import org.example.network.dto.User;
import org.example.utils.builders.abstracts.AbstractBuilder;
import org.example.utils.io.console.Console;

import java.io.IOException;

public class UserBuilder extends AbstractBuilder {

    /*private static MessageDigest MD;
    private final static String SALT;
    private final static String PEPPER;

    static {
        Properties properties = new Properties();
        try {
            properties.load(new FileInputStream("client\\src\\main\\resources\\application.properties"));
            SALT = properties.getProperty("SALT");
            PEPPER = properties.getProperty("PEPPER");
        } catch (IOException e) {
            throw new RuntimeException(e);

        }
    }
*/
    public static User build() throws IOException {
        return new User(
                inputUsername(),
                inputPassword()
        );
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
