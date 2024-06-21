package org.example.network.dto;

import org.example.network.model.Status;

import java.io.Serializable;

public record Response(Status status, String data) implements Serializable, Transfer {
}
