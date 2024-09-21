package org.example.exceptions.db;

import java.sql.SQLException;

public class UserAlreadyExistsSQLException extends SQLException {
    public UserAlreadyExistsSQLException() {
        super("Ooops! User already exists :(");
    }
}
