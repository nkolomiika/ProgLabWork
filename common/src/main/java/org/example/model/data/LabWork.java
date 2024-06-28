package org.example.model.data;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import lombok.*;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.concurrent.atomic.AtomicLong;

@Data
@RequiredArgsConstructor
@NoArgsConstructor
public class LabWork implements Comparable<LabWork>, Serializable {

    @Expose(deserialize = false)
    private long id; //Значение поля должно быть больше 0, Значение этого поля должно быть уникальным, Значение этого поля должно генерироваться автоматически

    @Expose
    @NonNull
    @SerializedName("name")
    private String name; //Поле не может быть null, Строка не может быть пустой

    @Expose
    @NonNull
    @SerializedName("coordinates")
    private Coordinates coordinates; //Поле не может быть null

    @Expose
    @NonNull
    @SerializedName("creation-date")
    private java.time.LocalDateTime creationDate; //Поле не может быть null, Значение этого поля должно генерироваться автоматически

    @Expose
    @SerializedName("minimal-point")
    private Integer minimalPoint; //Поле может быть null, Значение поля должно быть больше 0

    @Expose
    @NonNull
    @SerializedName("difficulty")
    private Difficulty difficulty; //Поле не может быть null

    @Expose
    @NonNull
    @SerializedName("author")
    private Person author; //Поле не может быть null

    public LabWork(
            @NonNull String name,
            @NonNull Coordinates coordinates,
            Integer minimalPoint,
            @NonNull Difficulty difficulty,
            @NonNull Person author) {
        this.id = IdCounter.getNextIdAndIncrement();
        this.name = name;
        this.coordinates = coordinates;
        this.creationDate = LocalDateTime.now();
        this.minimalPoint = minimalPoint;
        this.difficulty = difficulty;
        this.author = author;
    }

    /**
     * Функция сравнения двух объектов по полю difficulty
     * <br>> 0, если объект из параметра больше
     * <br>< 0, если объект из параметра меньше
     * <br>= 0, если объекты равны
     *
     * @param o the object to be compared.
     * @return int
     */
    @Override
    public int compareTo(LabWork o) {
        return o.getMinimalPoint() - this.getMinimalPoint();
    }
}