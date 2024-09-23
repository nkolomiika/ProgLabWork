package org.example.network.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.Serializable;

@Getter
@NoArgsConstructor
public class User implements Transfer, Serializable {
    private String username;
    private String password;

    public User(@NonNull String username, @NonNull String password) {
        this.username = username;
        this.password = password;
    }

    @Override
    public String toString() {
        return "User(" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ')';
    }
}
