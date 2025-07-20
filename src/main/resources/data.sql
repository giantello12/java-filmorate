-- MPA-рейтинги
INSERT INTO MpaRating (id, name) VALUES (1, 'G');
INSERT INTO MpaRating (id, name) VALUES (2, 'PG');
INSERT INTO MpaRating (id, name) VALUES (3, 'PG-13');
INSERT INTO MpaRating (id, name) VALUES (4, 'R');
INSERT INTO MpaRating (id, name) VALUES (5, 'NC-17');

-- Жанры
INSERT INTO Genre (id, name) VALUES (1, 'Комедия');
INSERT INTO Genre (id, name) VALUES (2, 'Драма');
INSERT INTO Genre (id, name) VALUES (3, 'Мультфильм');
INSERT INTO Genre (id, name) VALUES (4, 'Триллер');
INSERT INTO Genre (id, name) VALUES (5, 'Документальный');
INSERT INTO Genre (id, name) VALUES (6, 'Боевик');

-- Пользователи (с кавычками)
INSERT INTO "User" (id, email, login, name, birthday)
VALUES (1, 'user@example.com', 'user1', 'Иван Иванов', '1990-01-01');

INSERT INTO "User" (id, email, login, name, birthday)
VALUES (2, 'friend@example.com', 'user2', 'Петр Петров', '1995-05-15');

-- Фильмы
INSERT INTO Film (id, name, description, release_date, duration)
VALUES (1, 'Тестовый фильм 1', 'Описание первого тестового фильма', '2020-01-01', 120);

INSERT INTO Film (id, name, description, release_date, duration)
VALUES (2, 'Тестовый фильм 2', 'Описание второго тестового фильма', '2021-02-02', 90);

-- Связи фильмов с MPA
INSERT INTO FilmMpa (film_id, mpa_id) VALUES (1, 3);
INSERT INTO FilmMpa (film_id, mpa_id) VALUES (2, 1);

-- Связи фильмов с жанрами
INSERT INTO FilmGenres (film_id, genre_id) VALUES (1, 1);
INSERT INTO FilmGenres (film_id, genre_id) VALUES (1, 2);
INSERT INTO FilmGenres (film_id, genre_id) VALUES (2, 3);

-- Лайки (с кавычками в ссылке на User)
INSERT INTO FilmLikes (user_id, film_id) VALUES (1, 1);

-- Друзья (с кавычками в ссылках)
INSERT INTO Friendship (user_id, friend_id, status)
VALUES (1, 2, 'confirmed');