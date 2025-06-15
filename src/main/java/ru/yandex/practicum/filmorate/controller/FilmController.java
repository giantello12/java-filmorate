package ru.yandex.practicum.filmorate.controller;

import lombok.Getter;
import lombok.Setter;
import org.springframework.web.bind.annotation.*;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;

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

    public HashMap<Long, Film> films = new HashMap<>();

    @PostMapping
    public Film create(@RequestBody Film film) {
        log.info("Обработка запроса на добавление фильма");
        validateFilm(film);
        Long filmId = getNextId();
        film.setId(filmId);
        films.put(filmId, film);
        log.info("Добавлен фильм: ID={}, Название={}", film.getId(), film.getName());
        return film;
    }

    public HashMap<Long, Film> getFilms() {
        return films;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Обработка запроса на обновление фильма");
        if (film.getId() == null || films.get(film.getId()) == null) {
            String error = "Фильм с данным ID не существует!";
            log.error(error);
            throw new ValidationException(error);
        }
        validateFilm(film);
        films.put(film.getId(), film);
        return film;
    }

    @GetMapping
    public Collection<Film> allFilms() {
        log.info("Обработка запроса на получение всех фильмов");
        return films.values();
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isBlank()) {
            String error = "Название не должно быть пустым!";
            log.error(error);
            throw new ValidationException(error);
        }
        int maxDescriptionLength = 200;
        if (film.getDescription() == null || film.getDescription().length() > maxDescriptionLength) {
            String error = "Описание не должно быть длиннее " + maxDescriptionLength + " символов!";
            log.error(error);
            throw new ValidationException(error);
        }
        LocalDate earliestDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate() == null || film.getReleaseDate().isBefore(earliestDate)) {
            String error = "Дата не должна быть раньше " + earliestDate + "!";
            log.error(error);
            throw new ValidationException(error);
        }
        if (film.getDuration() <= 0) {
            String error = "Продолжительность фильма не может быть меньше 0!";
            log.error(error);
            throw new ValidationException(error);
        }
    }

    private long getNextId() {
        long currentMaxId = films.keySet()
                .stream()
                .mapToLong(id -> id)
                .max()
                .orElse(0);
        return ++currentMaxId;
    }
}
