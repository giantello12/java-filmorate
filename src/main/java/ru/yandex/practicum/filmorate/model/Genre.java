package ru.yandex.practicum.filmorate.model;

import lombok.Getter;

@Getter
public enum Genre {
    COMEDY("Комедия"),
    DRAMA("Драма"),
    ANIMATION("Мультфильм"),
    THRILLER("Триллер"),
    DOCUMENTARY("Документальный"),
    ACTION("Боевик");

    private final String name;

    Genre(String name) {
        this.name = name;
    }

}

