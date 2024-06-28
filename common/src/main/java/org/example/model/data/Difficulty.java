package org.example.model.data;

import com.google.gson.annotations.SerializedName;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import java.util.Arrays;
import java.util.StringJoiner;

@Getter
@ToString
@RequiredArgsConstructor
public enum Difficulty {
    @SerializedName("VERY_HARD")
    VERY_HARD(9),
    @SerializedName("IMPOSSIBLE")
    IMPOSSIBLE(99),
    @SerializedName("HOPELESS")
    HOPELESS(999);

    private final int level;

    public static String getAllValues() {
        StringJoiner nameList = new StringJoiner(", ");
        for (var difficulty : values())
            nameList.add(difficulty.name());

        return nameList.toString();
    }
}