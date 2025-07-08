package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;

import jakarta.validation.Valid;

import java.time.LocalDate;
import java.util.Collection;
import java.util.HashMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/films")
@Getter
@Setter
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);
    private final HashMap<Long, Film> films = new HashMap<>();
    private static final LocalDate EARLIEST_DATE = LocalDate.of(1895, 12, 28);

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        log.info("Обработка запроса на добавление фильма");

        if (film.getReleaseDate().isBefore(EARLIEST_DATE)) {
            String error = "Дата не должна быть раньше " + EARLIEST_DATE + "!";
            log.error(error);
            throw new ValidationException(error);
        }

        Long filmId = getNextId();
        film.setId(filmId);
        films.put(filmId, film);
        log.info("Добавлен фильм: ID={}, Название={}", film.getId(), film.getName());
        return film;
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        log.info("Обработка запроса на обновление фильма");

        if (film.getId() == null || !films.containsKey(film.getId())) {
            String error = "Фильм с данным ID не существует!";
            log.error(error);
            throw new ValidationException(error);
        }

        if (film.getReleaseDate().isBefore(EARLIEST_DATE)) {
            String error = "Дата не должна быть раньше " + EARLIEST_DATE + "!";
            log.error(error);
            throw new ValidationException(error);
        }

        films.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public Collection<Film> getFilms() {
        log.info("Обработка запроса на получение всех фильмов");
        return films.values();
    }

    private long getNextId() {
        return films.keySet().stream().mapToLong(id -> id).max().orElse(0) + 1;
    }
}