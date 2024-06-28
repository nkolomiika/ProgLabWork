package org.example.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@RequiredArgsConstructor
@AllArgsConstructor
public class Person implements Serializable {

    @Expose
    @NonNull
    @SerializedName("name")
    private String name; //Поле не может быть null, Строка не может быть пустой

    @Expose
    @SerializedName("weight")
    private Float weight; //Поле может быть null, Значение поля должно быть больше 0

    @Expose
    @NonNull
    @SerializedName("eye-color")
    private Color eyeColor; //Поле не может быть null

    @Expose
    @SerializedName("hair-color")
    private Color hairColor; //Поле может быть null

    @Expose
    @NonNull
    @SerializedName("nationality")
    private Country nationality; //Поле не может быть null
}
