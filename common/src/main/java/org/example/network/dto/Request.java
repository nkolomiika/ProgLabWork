package org.example.network.dto;

import lombok.*;
import org.example.model.data.LabWork;

import java.io.Serializable;

@Getter
@ToString
@AllArgsConstructor
@RequiredArgsConstructor
public class Request implements Serializable, Transfer {
    @NonNull
    private final String command;
    private String argument;
    @Setter
    private LabWork labWork;

    public Request(@NonNull String command, @NonNull String argument) {
        this.command = command;
        this.argument = argument;
    }
}
