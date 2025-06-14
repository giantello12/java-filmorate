package ru.yandex.practicum.filmorate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.controller.FilmController;

import java.time.Duration;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {

    private FilmController filmController;
    private Film validFilm;

    @BeforeEach
    void setUp() {
        filmController = new FilmController();
        validFilm = new Film(0L, "Valid Film", "Valid description",
                LocalDate.of(2000, 1, 1), Duration.ofMinutes(120));
    }

    @Test
    void addFilm_WithValidFilm_ShouldAddFilmAndReturnIt() {
        Film createdFilm = filmController.addFilm(validFilm);

        assertNotNull(createdFilm.getId(), "ID должен быть установлен");
        assertEquals(0, createdFilm.getId(), "Неверный ID фильма");
        assertEquals("Valid Film", createdFilm.getName(), "Название не совпадает");
        assertEquals("Valid description", createdFilm.getDescription(), "Описание не совпадает");
        assertEquals(LocalDate.of(2000, 1, 1), createdFilm.getReleaseDate(), "Дата релиза не совпадает");
        assertEquals(Duration.ofMinutes(120), createdFilm.getDuration(), "Продолжительность не совпадает");

        assertEquals(1, filmController.getFilms().size(), "Фильм не добавлен в хранилище");
    }

    @Test
    void addFilm_WithEmptyName_ShouldThrowValidationException() {
        Film emptyFilm = new Film(0L, "", "", LocalDate.now(), Duration.ofMinutes(1));
        emptyFilm.setDescription("");
        emptyFilm.setReleaseDate(LocalDate.now());
        emptyFilm.setDuration(Duration.ZERO);

        ValidationException exception = assertThrows(
                ValidationException.class,
                () -> filmController.addFilm(emptyFilm),
                "Ожидалось ValidationException при пустом названии фильма!"
        );

        assertEquals("Название не должно быть пустым!", exception.getMessage());
        assertTrue(filmController.getFilms().isEmpty(), "Фильм с пустым названием не должен добавляться");
    }
}
