package com.sgu.authservice.exception;

import com.sgu.authservice.dto.response.HttpResponseObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.Arrays;

@RestControllerAdvice
public class GlobalExceptionHandle extends ResponseEntityExceptionHandler {
    @ExceptionHandler(value = {NotFound.class})
    public ResponseEntity<HttpResponseObject> handleAccountNotExists(NotFound ex) {
       HttpResponseObject httpResponse = new HttpResponseObject()
               .builder()
               .code(HttpStatus.NOT_FOUND.value())
               .message(ex.getMessage())
               .build();

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(httpResponse);

    }

    @ExceptionHandler(value = {
            Fobidden.class
    })
    public ResponseEntity<HttpResponseObject> handleAccountFobidden(Fobidden ex) {
        HttpResponseObject httpResponse = new HttpResponseObject()
                .builder()
                .code(HttpStatus.FORBIDDEN.value())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(httpResponse);

    }

    @ExceptionHandler(value = {
            BadRequest.class
    })
    public ResponseEntity<HttpResponseObject> handleAccountBadRequestException(BadRequest ex) {
        HttpResponseObject httpResponse = new HttpResponseObject()
                .builder()
                .code(HttpStatus.BAD_REQUEST.value())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(httpResponse);

    }


    @ExceptionHandler(value = {
            RefreshTokenIsExpired.class
    })
    public ResponseEntity<HttpResponseObject> handleRefreshTokenIsExpiredException(RefreshTokenIsExpired ex) {
        HttpResponseObject httpResponse = new HttpResponseObject()
                .builder()
                .code(HttpStatus.UNAUTHORIZED.value())
                .message(ex.getMessage())
                .build();

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(httpResponse);

    }
}
