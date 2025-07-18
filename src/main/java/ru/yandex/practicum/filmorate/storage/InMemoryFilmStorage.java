package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;


@Component("inMemoryFilmStorage")
public class InMemoryFilmStorage implements FilmStorage {
    private final Map<Long, Film> films = new HashMap<>();
    private long nextId = 1;

    @Override
    public Film create(Film film) {
        film.setId(nextId++);
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film update(Film film) {
        if (!films.containsKey(film.getId())) {
            throw new NotFoundException("Фильм", film.getId());
        }
        films.put(film.getId(), film);
        return film;
    }

    @Override
    public Film getFilmById(long id) {
        if (!films.containsKey(id)) {
            throw new NotFoundException("Фильм", id);
        }
        return films.get(id);
    }

    @Override
    public Collection<Film> getAllFilms() {
        return films.values();
    }
}
