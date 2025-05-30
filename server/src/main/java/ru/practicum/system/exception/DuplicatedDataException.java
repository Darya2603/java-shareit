package ru.practicum.system.exception;

public class DuplicatedDataException extends RuntimeException {
    public DuplicatedDataException(String message) {
        super(message);
    }
}