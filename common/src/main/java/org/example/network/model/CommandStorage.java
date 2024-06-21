package org.example.network.model;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;

public record CommandStorage(@NonNull String command, String argument){
}
