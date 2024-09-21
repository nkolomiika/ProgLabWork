package org.example.commands.abstarct;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import org.example.network.dto.Request;
import org.example.network.dto.Response;
import org.example.network.model.ArgumentType;
import org.example.network.model.Role;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
public abstract class  Command {
    private final String name;
    private final String description;
    private final ArgumentType argumentType;
    private final Role role;

    public Command(String name, String description) {
        this.name = name;
        this.description = description;
        this.argumentType = ArgumentType.NONE;
        this.role = Role.USER;
    }

    public Command(String name, String description, Role role) {
        this.name = name;
        this.description = description;
        this.argumentType = ArgumentType.NONE;
        this.role = role;
    }

    public abstract Response execute(Request request);
}
