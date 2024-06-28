package org.example.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@RequiredArgsConstructor
public class Coordinates implements Serializable {
    @Expose
    @SerializedName("x")
    private Integer x; //Максимальное значение поля:583

    @Expose
    @NonNull
    @SerializedName("y")
    private Float y; //Поле не может быть null
}