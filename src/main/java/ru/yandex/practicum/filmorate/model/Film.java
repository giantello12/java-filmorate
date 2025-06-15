package ru.yandex.practicum.filmorate.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class Film {
    Long id;

    @NotBlank(message = "Название не должно быть пустым!")
    private String name;

    @Size(max = 200, message = "Описание не должно быть длиннее 200 символов!")
    private String description;

    private LocalDate releaseDate;

    @Positive(message = "Продолжительность фильма должна быть положительной!")
    private int duration;
}