package dev.vetyutnev.ministudycrud;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorMessageResponse> handleValidationException(MethodArgumentNotValidException e){

        String detailedMessage = e.getBindingResult().getFieldErrors().stream()
                .map(error -> error.getField() + ": " + error.getDefaultMessage())
                .collect(Collectors.joining(", "));

        log.warn("ошибка валидации: {}", detailedMessage);

        return ResponseEntity.badRequest().body(new ErrorMessageResponse(
                "ошибка валидации",
                detailedMessage,
                LocalDateTime.now()
        ));
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ErrorMessageResponse> handleUserNotFoundException(UserNotFoundException e){
        log.warn("ошибка при получении пользователя: {}", e.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                new ErrorMessageResponse(
                        "не найдено",
                        e.getMessage(),
                        LocalDateTime.now()
                )
        );
    }
}
