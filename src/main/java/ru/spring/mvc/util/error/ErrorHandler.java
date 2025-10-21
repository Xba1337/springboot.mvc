package ru.spring.mvc.util.error;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.NoSuchElementException;

@ControllerAdvice
public class ErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(ErrorHandler.class);

    @ExceptionHandler(NoSuchElementException.class)
    private ResponseEntity<ServerErrorDto> handleNoSuchElementException(NoSuchElementException e) {
        log.error(e.getMessage());
        ServerErrorDto errorMessage = new ServerErrorDto(
                "Entity is not found",
                e.getMessage(),
                LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    private ResponseEntity<ServerErrorDto> handleIllegalArgumentException(IllegalArgumentException e) {
        log.error(e.getMessage());
        ServerErrorDto errorMessage = new ServerErrorDto(
                "Wrong request parameters",
                e.getMessage(),
                LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(errorMessage);
    }

    @ExceptionHandler(Exception.class)
    private ResponseEntity<ServerErrorDto> handleException(Exception e) {
        log.error(e.getMessage());
        ServerErrorDto errorMessage = new ServerErrorDto(
                "Server error",
                e.getMessage(),
                LocalDateTime.now());

        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorMessage);
    }

}
