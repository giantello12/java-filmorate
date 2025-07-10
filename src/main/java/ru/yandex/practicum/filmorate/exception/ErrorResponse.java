package ru.yandex.practicum.filmorate.exception;

import lombok.Getter;

@Getter
public class ErrorResponse {
    private final String error;
    private final String message;

    public ErrorResponse(String message) {
        this.error = "runtime_error";
        this.message = message;
    }
}