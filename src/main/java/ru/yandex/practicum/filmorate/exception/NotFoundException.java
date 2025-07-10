package ru.yandex.practicum.filmorate.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String objectType, Object identifier) {
        super(String.format("%s с ID %s не найден", objectType, identifier));
    }
}