package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.controller.FilmController;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryFilmStorage;
import ru.yandex.practicum.filmorate.storage.InMemoryUserStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController filmController;
    private Film validFilm;

    @BeforeEach
    void setUp() {
        FilmStorage filmStorage = new InMemoryFilmStorage();
        UserStorage userStorage = new InMemoryUserStorage();
        filmController = new FilmController(filmStorage, new FilmService(filmStorage, userStorage));
        validFilm = new Film(0L, "Valid Film", "Valid description",
                LocalDate.of(2000, 1, 1), 120);
    }

    @Test
    void addFilm_WithValidFilm_ShouldAddFilmAndReturnIt() {
        Film createdFilm = filmController.create(validFilm);

        assertNotNull(createdFilm.getId(), "ID должен быть установлен");
        assertEquals(1, createdFilm.getId(), "Неверный ID фильма");
        assertEquals("Valid Film", createdFilm.getName(), "Название не совпадает");
        assertEquals("Valid description", createdFilm.getDescription(), "Описание не совпадает");
        assertEquals(LocalDate.of(2000, 1, 1), createdFilm.getReleaseDate(), "Дата релиза не совпадает");
        assertEquals(120, createdFilm.getDuration(), "Продолжительность не совпадает");
        assertEquals(1, filmController.getFilms().size(), "Фильм не добавлен в хранилище");
    }


}
