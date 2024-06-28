package org.example.model.data;

import com.google.gson.annotations.SerializedName;

import java.util.StringJoiner;

public enum Color {
    @SerializedName("GREEN")
    GREEN,
    @SerializedName("BLACK")
    BLACK,
    @SerializedName("WHITE")
    WHITE,
    @SerializedName("RED")
    RED,
    @SerializedName("BLUE")
    BLUE,
    @SerializedName("ORANGE")
    ORANGE;

    public static String getAllValues(){
        StringJoiner nameList = new StringJoiner(", ");
        for (Color color: values())
            nameList.add(color.name());

        return nameList.toString();
    }
}