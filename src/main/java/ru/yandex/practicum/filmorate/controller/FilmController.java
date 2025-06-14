package ru.yandex.practicum.filmorate.controller;

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
public class FilmController {
    private static final Logger log = LoggerFactory.getLogger(FilmController.class);

    public HashMap<Long, Film> films = new HashMap<>();

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        log.info("Обработка запроса на добавление фильма");
        validateFilm(film);
        films.put(getNextId(), film);
        return film;
    }

    public HashMap<Long, Film> getFilms() {
        return films;
    }

    public void setFilms(HashMap<Long, Film> films) {
        this.films = films;
    }

    @PutMapping
    public Film update(@RequestBody Film film) {
        log.info("Обработка запроса на обновление фильма");
        if (films.get(film.getId()) == null || film.getId() == null) {
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
        if (film.getName().isBlank()) {
            String error = "Название не должно быть пустым!";
            log.error(error);
            throw new ValidationException(error);
        }
        int maxDescriptionLength = 200;
        if (film.getDescription().length() > maxDescriptionLength) {
            String error = "Описание не должно быть длиннее " + maxDescriptionLength + " символов!";
            log.error(error);
            throw new ValidationException(error);
        }
        LocalDate earliestDate = LocalDate.of(1895, 12, 28);
        if (film.getReleaseDate().isBefore(earliestDate)) {
            String error = "Дата не должна быть раньше " + earliestDate + "!";
            log.error(error);
            throw new ValidationException(error);
        }
        if (!film.getDuration().isPositive()) {
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
