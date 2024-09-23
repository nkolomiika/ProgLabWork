package org.example.runners;

import org.example.network.dto.User;
import org.example.network.model.RuntimeMode;

public record Pair(RuntimeMode runtimeMode, User user) {
}
