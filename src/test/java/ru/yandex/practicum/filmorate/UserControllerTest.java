package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.controller.UserController;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {

    private UserController userController;
    private User validUser;

    @BeforeEach
    void setUp() {
        userController = new UserController();
        validUser = new User(0L, "valid@example.com", "valid_login", "Valid User", LocalDate.of(2000, 1, 1));
    }

    @Test
    void create_WithValidUser_ShouldAddUserAndReturnIt() {
        User createdUser = userController.create(validUser);
        assertNotNull(createdUser.getId(), "ID должен быть установлен");
        assertEquals(1, createdUser.getId(), "Неверный ID пользователя");
        assertEquals("valid@example.com", createdUser.getEmail(), "Email не совпадает");
        assertEquals("valid_login", createdUser.getLogin(), "Логин не совпадает");
        assertEquals("Valid User", createdUser.getName(), "Имя не совпадает");
        assertEquals(LocalDate.of(2000, 1, 1), createdUser.getBirthday(), "Дата рождения не совпадает");
    }
}