package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.service.FilmService;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@RestController
@RequestMapping("/films")
public class FilmController {
    private final FilmStorage storage;
    private final FilmService service;
    private static final LocalDate EARLIEST_DATE = LocalDate.of(1895, 12, 28);

    @Autowired
    public FilmController(FilmStorage storage, FilmService service) {
        this.storage = storage;
        this.service = service;
    }

    @PostMapping
    public Film create(@Valid @RequestBody Film film) {
        validateReleaseDate(film);
        return storage.create(film);
    }

    @PutMapping
    public Film update(@Valid @RequestBody Film film) {
        validateReleaseDate(film);
        return storage.update(film);
    }

    @GetMapping
    public Collection<Film> getFilms() {
        return storage.getAllFilms();
    }

    @PutMapping("/{id}/like/{userId}")
    public void addLike(
            @Positive
            @PathVariable long id,
            @Positive
            @PathVariable long userId) {
        service.addLike(id, userId);
    }

    @DeleteMapping("/{id}/like/{userId}")
    public void removeLike(
            @Positive
            @PathVariable long id,
            @Positive
            @PathVariable long userId) {
        service.removeLike(id, userId);
    }

    @GetMapping("/popular")
    public List<Film> getPopularFilms(
            @RequestParam(defaultValue = "10") int count) {
        return service.getPopularFilms(count);
    }

    @GetMapping("/{id}")
    public Film getFilmById(@PathVariable long id) {
        return storage.getFilmById(id);
    }

    private void validateReleaseDate(Film film) {
        if (film.getReleaseDate().isBefore(EARLIEST_DATE)) {
            String error = "Дата не должна быть раньше " + EARLIEST_DATE + "!";
            throw new ValidationException(error);
        }
    }
}