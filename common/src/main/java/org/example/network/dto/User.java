package org.example.network.dto;

import lombok.Getter;
import lombok.NonNull;

import java.io.Serializable;

@Getter
public class User implements Transfer, Serializable {
    private String username;
    private String password;

    public User(@NonNull String username, @NonNull String password) {
        this.username = username;
        this.password = password;
    }

    private User() {
    }

    public static User createEmptyUser() {
        return new User();
    }
}
