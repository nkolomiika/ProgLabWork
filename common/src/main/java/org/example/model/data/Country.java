package org.example.model.data;

import com.google.gson.annotations.SerializedName;

import java.util.StringJoiner;

public enum Country {
    @SerializedName("RUSSIA")
    RUSSIA,
    @SerializedName("USA")
    USA,
    @SerializedName("ITALY")
    ITALY,
    @SerializedName("THAILAND")
    THAILAND,
    @SerializedName("SOUTH_KOREA")
    SOUTH_KOREA;

    public static String getAllValues() {
        StringJoiner nameList = new StringJoiner(", ");
        for (var country : values())
            nameList.add(country.name());

        return nameList.toString();
    }
}