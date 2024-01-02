package com.cydeo.exception;

import com.cydeo.annotation.DefaultExceptionMessage;
import com.cydeo.dto.DefaultExceptionMessageDTO;
import com.cydeo.dto.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(TicketingProjectException.class)
    public ResponseEntity<ResponseWrapper> serviceException(TicketingProjectException exception) {
        String message = exception.getMessage();
        return new ResponseEntity<>(ResponseWrapper.builder()
                .success(false)
                .code(HttpStatus.CONFLICT.value())
                .message(message).build(),
                HttpStatus.CONFLICT);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ResponseWrapper> accessDeniedException(AccessDeniedException exception){
        String message = exception.getMessage();
        return new ResponseEntity<>(ResponseWrapper.builder()
                .success(false)
                .code(HttpStatus.FORBIDDEN.value())
                .message(message).build(),
                HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<ValidationExceptionResponse> validationExceptionHandler(MethodArgumentNotValidException exception){
        exception.printStackTrace();

        ValidationExceptionResponse response = new ValidationExceptionResponse("Invalid input(s)", HttpStatus.BAD_REQUEST);

        List<ValidationException> validationErrors = new ArrayList<>();

        for (ObjectError error : exception.getBindingResult().getAllErrors()) {
            String fieldName = ((FieldError) error).getField();
            Object rejectedValue = ((FieldError) error).getRejectedValue();
            String errorMessage = error.getDefaultMessage();

            ValidationException validationException = new ValidationException(fieldName, rejectedValue, errorMessage);
            validationErrors.add(validationException);
        }

        response.setValidationExceptions(validationErrors);
        response.setErrorCount(validationErrors.size());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(response);
    }

    @ExceptionHandler({
            Exception.class,
            RuntimeException.class,
            Throwable.class,
            BadCredentialsException.class
    })
    public ResponseEntity<ResponseWrapper> genericException(Throwable throwable, HandlerMethod handlerMethod) {
        Optional<DefaultExceptionMessageDTO> defaultMessage = getMessageFromAnnotation(handlerMethod.getMethod());
        if (defaultMessage.isPresent() && !ObjectUtils.isEmpty(defaultMessage.get().getMessage())) { //if there is a default message it will show up
            ResponseWrapper response = ResponseWrapper.builder()
                    .success(false)
                    .message(defaultMessage.get().getMessage())
                    .code(HttpStatus.INTERNAL_SERVER_ERROR.value())
                    .build();
            return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
        }
        return new ResponseEntity<>(ResponseWrapper.builder()
                .success(false)
                .message("Action failed: An error occurred!")
                .code(HttpStatus.INTERNAL_SERVER_ERROR.value()).build(),
                HttpStatus.INTERNAL_SERVER_ERROR);
    }


    private Optional<DefaultExceptionMessageDTO> getMessageFromAnnotation(Method method) {
        DefaultExceptionMessage defaultExceptionMessage = method.getAnnotation(DefaultExceptionMessage.class);
        if (defaultExceptionMessage != null) {
            DefaultExceptionMessageDTO defaultExceptionMessageDto = DefaultExceptionMessageDTO
                    .builder()
                    .message(defaultExceptionMessage.defaultMessage())
                    .build();
            return Optional.of(defaultExceptionMessageDto);
        }
        return Optional.empty();
    }
}