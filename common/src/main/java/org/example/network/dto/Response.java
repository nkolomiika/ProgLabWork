package org.example.network.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import org.example.network.model.Status;
import org.example.network.model.RuntimeMode;

import java.io.Serializable;

@ToString
@Getter
@AllArgsConstructor
public class Response implements Serializable, Transfer {
    private Status status;
    private String data;
    private RuntimeMode runtimeMode;

    public Response(Status status, String data) {
        this.status = status;
        this.data = data;
        this.runtimeMode = RuntimeMode.CONSOLE;
    }
}
