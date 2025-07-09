package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.service.UserService;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserStorage storage;
    private final UserService service;

    @Autowired
    public UserController(UserStorage userStorage, UserService userService) {
        this.storage = userStorage;
        this.service = userService;
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {

        validateUser(user);
        setLoginAsNameIfBlank(user);

        return storage.create(user);
    }

    @PutMapping
    public User update(@Valid @RequestBody User user) {
        validateUser(user);
        setLoginAsNameIfBlank(user);

        return storage.update(user);
    }

    @GetMapping
    public Collection<User> getUsers() {
        return storage.getAllUsers();
    }

    @PutMapping("/{id}/friends/{friendId}")
    public void addFriend(@PathVariable long id, @PathVariable long friendId) {
        service.addFriend(id, friendId);
    }

    @DeleteMapping("/{id}/friends/{friendId}")
    public void removeFriend(@PathVariable long id, @PathVariable long friendId) {
        service.removeFriend(id, friendId);
    }

    @GetMapping("/{id}/friends")
    public List<User> getFriends(@PathVariable long id) {
        return service.getUserFriends(id);
    }

    @GetMapping("/{id}/friends/common/{otherId}")
    public List<User> getCommonFriends(
            @PathVariable long id,
            @PathVariable long otherId) {
        return service.getCommonFriends(id, otherId);
    }

    private void validateUser(User user) {
        if (user.getLogin().contains(" ")) {
            String error = "Логин не должен содержать пробелы!";
            throw new ValidationException(error);
        }
    }

    private void setLoginAsNameIfBlank(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
    }
}