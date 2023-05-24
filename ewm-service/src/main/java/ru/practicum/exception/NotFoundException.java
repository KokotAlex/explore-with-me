package ru.practicum.exception;

public class NotFoundException extends RuntimeException {

    public NotFoundException(String objectName, Long id) {
        super(String.format("%s with id: %d does not exist", objectName, id));
    }

    public NotFoundException(String message) {
        super(message);
    }
}
