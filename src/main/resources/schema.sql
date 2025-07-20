CREATE TABLE IF NOT EXISTS "User" (
    id INT AUTO_INCREMENT PRIMARY KEY,
    email VARCHAR(255) NOT NULL,
    login VARCHAR(255) NOT NULL,
    name VARCHAR(255),
    birthday DATE
);

CREATE TABLE IF NOT EXISTS MpaRating (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

CREATE TABLE IF NOT EXISTS Genre (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS Film (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    description TEXT,
    release_date DATE,
    duration INT
);

CREATE TABLE IF NOT EXISTS FilmMpa (
    film_id INT,
    mpa_id INT,
    PRIMARY KEY (film_id, mpa_id),
    FOREIGN KEY (film_id) REFERENCES Film(id) ON DELETE CASCADE,
    FOREIGN KEY (mpa_id) REFERENCES MpaRating(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS FilmGenres (
    film_id INT,
    genre_id INT,
    PRIMARY KEY (film_id, genre_id),
    FOREIGN KEY (film_id) REFERENCES Film(id) ON DELETE CASCADE,
    FOREIGN KEY (genre_id) REFERENCES Genre(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS FilmLikes (
    user_id INT,
    film_id INT,
    PRIMARY KEY (user_id, film_id),
    FOREIGN KEY (user_id) REFERENCES "User"(id) ON DELETE CASCADE,
    FOREIGN KEY (film_id) REFERENCES Film(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS Friendship (
    user_id INT,
    friend_id INT,
    status VARCHAR(20) NOT NULL,
    PRIMARY KEY (user_id, friend_id),
    FOREIGN KEY (user_id) REFERENCES "User"(id) ON DELETE CASCADE,
    FOREIGN KEY (friend_id) REFERENCES "User"(id) ON DELETE CASCADE
);