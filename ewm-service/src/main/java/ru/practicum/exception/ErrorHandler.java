package ru.practicum.exception;

import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestValueException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.List;

import static java.time.LocalDateTime.now;

@Slf4j
@RestControllerAdvice
public class ErrorHandler {

    @ExceptionHandler
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiError handleInternalException(Throwable exception) {
        log.error("500: {}", exception.getMessage(), exception);

        return createApiError("INTERNAL_SERVER_ERROR",
                "Something went wrong. Contact Support",
                exception);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class,
            BadRequestException.class,
            MissingRequestValueException.class})
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiError incorrectParameterException(RuntimeException exception) {
        log.error("400: {}", exception.getMessage(), exception);

        return createApiError("Bad Request", "Incorrectly made request.", exception);
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ApiError notFoundException(NotFoundException exception) {
        log.error("404: {}", exception.getMessage(), exception);

        return createApiError("Not Found",
                "The required object was not found.",
                exception);
    }

    @ExceptionHandler({DataIntegrityViolationException.class, ConflictException.class})
    @ResponseStatus(HttpStatus.CONFLICT)
    public ApiError currentStateIsIncompatibleException(RuntimeException exception) {
        log.error("409: {}", exception.getMessage(), exception);

        return createApiError("CONFLICT",
                "Integrity constraint has been violated.",
                exception);
    }

    private ApiError createApiError(String status, String reason, Throwable exception) {
        return new ApiError(status,
                reason,
                exception.getMessage(),
                now().toString(),
                List.of(Arrays.toString(exception.getStackTrace())));
    }

    @Getter
    @Setter
    @AllArgsConstructor
    private static class ApiError {

        // Код статуса HTTP-ответа
        private String status;

        // Общее описание причины ошибки.
        private String reason;

        // Сообщение об ошибке.
        private String message;

        // Дата и время когда произошла ошибка (в формате "yyyy-MM-dd HH:mm:ss")
        private String timestamp;

        // Список стектрейсов или описания ошибок
        private List<String> errors;

    }
}