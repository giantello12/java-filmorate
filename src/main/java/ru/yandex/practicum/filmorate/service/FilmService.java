package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class FilmService {
    private final FilmStorage filmStorage;
    private final UserStorage userStorage;

    @Autowired
    public FilmService(FilmStorage filmStorage, UserStorage userStorage) {
        this.filmStorage = filmStorage;
        this.userStorage = userStorage;
    }

    public void addLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        validateUserExistence(userId);
        if (film.getLikes().contains(userId)) {
            throw new ValidationException("Пользователь уже поставил лайк этому фильму!");
        }
        film.getLikes().add(userId);
    }

    public void removeLike(long filmId, long userId) {
        Film film = getFilmById(filmId);
        validateUserExistence(userId);
        if (!film.getLikes().contains(userId)) {
            throw new ValidationException("Лайк от данного пользователя не найден");
        }
        film.getLikes().remove(userId);
    }

    public List<Film> getPopularFilms(int count) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.comparingInt((Film f) -> f.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    private Film getFilmById(long id) {
        return filmStorage.getAllFilms().stream()
                .filter(f -> f.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Фильм", id));
    }

    private void validateUserExistence(long id) {
        userStorage.getAllUsers().stream()
                .filter(u -> u.getId() == id)
                .findFirst()
                .orElseThrow(() -> new NotFoundException("Пользователь", id));
    }
}
