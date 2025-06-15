package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private final HashMap<Long, User> users = new HashMap<>();

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        log.info("Обработка запроса на создание пользователя");

        if (user.getLogin().contains(" ")) {
            String error = "Логин не должен содержать пробелы!";
            log.error(error);
            throw new ValidationException(error);
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        Long userId = getNextId();
        user.setId(userId);
        users.put(userId, user);
        return user;
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        log.info("Обработка запроса на обновление пользователя");

        if (user.getId() == null || !users.containsKey(user.getId())) {
            String error = "Пользователь с данным ID не существует!";
            log.error(error);
            throw new ValidationException(error);
        }

        if (user.getLogin().contains(" ")) {
            String error = "Логин не должен содержать пробелы!";
            log.error(error);
            throw new ValidationException(error);
        }

        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }

        users.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public Collection<User> allUsers() {
        log.info("Обработка запроса на получение всех пользователей");
        return users.values();
    }

    private long getNextId() {
        return users.keySet().stream().mapToLong(id -> id).max().orElse(0) + 1;
    }
}