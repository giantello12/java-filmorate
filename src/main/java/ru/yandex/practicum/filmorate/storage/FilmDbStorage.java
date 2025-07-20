package ru.yandex.practicum.filmorate.storage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.MpaRating;

import java.sql.PreparedStatement;
import java.util.*;

@Repository
@Qualifier("filmDbStorage")
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public FilmDbStorage(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public Film create(Film film) {
        String sql = "INSERT INTO films (name, description, release_date, duration, mpa_rating) " +
                "VALUES (?, ?, ?, ?, ?)";

        KeyHolder keyHolder = new GeneratedKeyHolder();

        jdbcTemplate.update(connection -> {
            PreparedStatement stmt = connection.prepareStatement(sql, new String[]{"id"});
            stmt.setString(1, film.getName());
            stmt.setString(2, film.getDescription());
            stmt.setDate(3, java.sql.Date.valueOf(film.getReleaseDate()));
            stmt.setInt(4, film.getDuration());
            stmt.setString(5, film.getMpa().name());
            return stmt;
        }, keyHolder);

        long filmId = Objects.requireNonNull(keyHolder.getKey()).longValue();

        if (film.getGenres() != null && !film.getGenres().isEmpty()) {
            updateFilmGenres(filmId, film.getGenres());
        }

        return getFilmById(filmId);
    }

    @Override
    public Film update(Film film) {
        String sql = "UPDATE films SET name = ?, description = ?, release_date = ?, " +
                "duration = ?, mpa_rating = ? WHERE id = ?";

        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getMpa().name(),
                film.getId());

        // Обновляем жанры
        updateFilmGenres(film.getId(), film.getGenres());

        return getFilmById(film.getId()); // Возвращаем обновленный фильм
    }

    @Override
    public Collection<Film> getAllFilms() {
        String sql = "SELECT f.* FROM films f";
        return jdbcTemplate.query(sql, (rs, rowNum) -> {
            long filmId = rs.getLong("id");
            Set<Genre> genres = getFilmGenresFromDb(filmId);

            return new Film(
                    filmId,
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDate("release_date").toLocalDate(),
                    rs.getInt("duration"),
                    genres,
                    MpaRating.valueOf(rs.getString("mpa_rating"))
            );
        });
    }

    @Override
    public Film getFilmById(long id) {
        String sql = "SELECT f.* FROM films f WHERE f.id = ?";
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            Set<Genre> genres = getFilmGenresFromDb(id);

            return new Film(
                    rs.getLong("id"),
                    rs.getString("name"),
                    rs.getString("description"),
                    rs.getDate("release_date").toLocalDate(),
                    rs.getInt("duration"),
                    genres,
                    MpaRating.valueOf(rs.getString("mpa_rating"))
            );
        }, id);
    }

    private Set<Genre> getFilmGenresFromDb(long filmId) {
        String sql = "SELECT genre FROM film_genres WHERE film_id = ?";
        return new HashSet<>(jdbcTemplate.query(sql,
                (rs, rowNum) -> Genre.valueOf(rs.getString("genre")),
                filmId));
    }

    private void updateFilmGenres(long filmId, Set<Genre> genres) {
        jdbcTemplate.update("DELETE FROM film_genres WHERE film_id = ?", filmId);
        if (genres != null && !genres.isEmpty()) {
            String sql = "INSERT INTO film_genres (film_id, genre) VALUES (?, ?)";
            for (Genre genre : genres) {
                jdbcTemplate.update(sql, filmId, genre.name());
            }
        }
    }
}