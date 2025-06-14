package ru.yandex.practicum.filmorate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

@RestController
@RequestMapping("/users")
public class UserController {
    private static final Logger log = LoggerFactory.getLogger(UserController.class);
    private HashMap<Long, User> users = new HashMap<>();

    public HashMap<Long, User> getUsers() {
        return users;
    }

    public void setUsers(HashMap<Long, User> users) {
        this.users = users;
    }

    @PostMapping
    public User create(@RequestBody User user) {
        log.info("Обработка запроса на создание пользователя");
        validateUser(user);
        users.put(getNextId(), user);
        return user;
    }

    @PutMapping
    public User update(@RequestBody User user) {
        log.info("Обработка запроса на обновление пользователя");
        if (users.get(user.getId()) == null || user.getId() == null) {
            String error = "Пользователь с данным ID не существует!";
            log.error(error);
            throw new ValidationException(error);
        }
        validateUser(user);
        users.put(user.getId(), user);
        return user;
    }

    @GetMapping
    public Collection<User> allUsers() {
        log.info("Обработка запроса на получение всех пользователей");
        return users.values();
    }

    private void validateUser(User user) {
        if (user.getEmail().isBlank() || !user.getEmail().contains("@")) {
            String error = "Некорректный email!";
            log.error(error);
            throw new ValidationException(error);
        }
        if (user.getLogin().isBlank() || user.getLogin().contains(" ")) {
            String error = "Некорректный login!";
            log.error(error);
            throw new ValidationException(error);
        }
        if (user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            String error = "Дата рождения не может быть в будущем!";
            log.error(error);
            throw new ValidationException(error);
        }
    }

    private long getNextId() {
        long currentMaxId = users.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
