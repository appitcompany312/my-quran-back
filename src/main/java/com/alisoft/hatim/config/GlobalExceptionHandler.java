package com.alisoft.hatim.config;

import com.alisoft.hatim.dto.error.*;
import com.alisoft.hatim.exception.*;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.exception.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.*;

@Slf4j
@RestControllerAdvice
class GlobalExceptionHandler {
    private static final String RESOURCE_NOT_FOUND = "Resource not found";
    private static final String CONFLICT = "An error occurred, please try again later";
    private static final String DUPLICATE = "Duplicate data";
    private static final String UNPROCESSABLE = "Unprocessable entity";
    private static final String UNAUTHORIZED = "Unauthorized";
    private static final String ACCESS_DENIED = "Access denied";

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(NotFoundException.class)
    NotFoundErrorResponseDto handleNotFoundErrorResponseDto(NotFoundException exception) {
        String message = getMessage(exception, RESOURCE_NOT_FOUND);
        log.error(message);
        return new NotFoundErrorResponseDto(message);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(ConflictException.class)
    ConflictErrorResponseDto handleConflictErrorResponseDto(ConflictException exception) {
        String message = getMessage(exception, CONFLICT);
        log.error(message);
        return new ConflictErrorResponseDto(message);
    }

    @ResponseStatus(HttpStatus.CONFLICT)
    @ExceptionHandler(DuplicateException.class)
    DuplicateErrorResponseDto handleDuplicateException(DuplicateException exception) {
        String message = getMessage(exception, DUPLICATE);
        log.error(message);
        return new DuplicateErrorResponseDto(message);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HttpMessageNotReadableException.class)
    UnprocessableEntityErrorResponseDto handleHttpMessageNotReadableException(HttpMessageNotReadableException exception) {
        String message = getMessage(exception, UNPROCESSABLE);
        log.error(message);
        return new UnprocessableEntityErrorResponseDto(message);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(ConstraintViolationException.class)
    UnprocessableEntityErrorResponseDto handleConstraintViolationException(ConstraintViolationException exception) {
        String message = getMessage(exception, UNPROCESSABLE);
        log.error(message);
        return new UnprocessableEntityErrorResponseDto(message);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(NullPointerException.class)
    NullPointerErrorResponseDto handleNullPointerException(NullPointerException exception) {

        exception.printStackTrace();
        return new NullPointerErrorResponseDto(Arrays.toString(exception.getStackTrace()));
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(PermissionDeniedException.class)
    AccessDeniedErrorResponseDto permissionDeniedException(PermissionDeniedException exception) {

        String message = getMessage(exception, ACCESS_DENIED);
        log.error(message);
        return new AccessDeniedErrorResponseDto(message);
    }

    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(NotValidArgumentException.class)
    NotValidArgumentErrorResponseDto handleIllegalArgumentException(NotValidArgumentException exception) {

        exception.printStackTrace();
        return new NotValidArgumentErrorResponseDto(exception.getField(), exception.getMessage());
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(UnAuthorizedException.class)
    UnauthorizedErrorResponseDto handleUnauthorizedExceptionException(UnAuthorizedException exception) {
        String message = getMessage(exception, UNAUTHORIZED);
        log.error(message);
        return new UnauthorizedErrorResponseDto(message);
    }


    @ResponseStatus(HttpStatus.UNPROCESSABLE_ENTITY)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    NotValidArgumentErrorResponseDto handleMethodArgumentNotValidException(MethodArgumentNotValidException exc) {
        List<FieldError> fieldErrors = exc
                .getBindingResult()
                .getFieldErrors();

        if (CollectionUtils.isEmpty(fieldErrors)) {
            return new NotValidArgumentErrorResponseDto("unknown", exc.getMessage());
        }

        Map<String, String> resultErrors = new HashMap<>();

        fieldErrors.forEach((item) -> resultErrors.put(item.getField(), item.getDefaultMessage()));

        return new NotValidArgumentErrorResponseDto(resultErrors);
    }

    private String getMessage(Exception exception, String defaultMessage) {
        String message = defaultMessage;

        if (Objects.nonNull(exception.getLocalizedMessage())) {
            message = exception.getLocalizedMessage();
        }
        if (Objects.nonNull(exception.getMessage())) {
            message = exception.getMessage();
        }

        return message;
    }
}
