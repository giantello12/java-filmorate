package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
public class User {
    Long id;

    @Email(message = "Некорректный email!")
    @NotBlank(message = "Email не должен быть пустым!")
    private String email;

    @NotBlank(message = "Логин не должен быть пустым!")
    private String login;

    private String name;

    @PastOrPresent(message = "Дата рождения не может быть в будущем!")
    private LocalDate birthday;

    private final Set<Long> friends = new HashSet<>();

    private Friendship friendship;
}

enum Friendship {
    CONFIRMED, SENT
}