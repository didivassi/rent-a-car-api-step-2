package academy.mindswap.rentacarapi.controller;


import academy.mindswap.rentacarapi.exceptions.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.dao.NonTransientDataAccessException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import academy.mindswap.rentacarapi.error.Error;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.net.ConnectException;
import java.util.Date;

import static org.springframework.http.HttpStatus.*;

@ControllerAdvice
public class ExceptionsHandler {


    @ExceptionHandler(value = {
            NotFoundException.class
    })
    public ResponseEntity<Error> dealWithNotFound(Exception e, HttpServletRequest request){
        Error error = buildError(e, request, NOT_FOUND.toString());
        return new ResponseEntity<>(error, NOT_FOUND);
    }

    @ExceptionHandler(value = {
            DuplicateKeyException.class,
            DataIntegrityViolationException.class,
            NonTransientDataAccessException.class
    })
    public ResponseEntity<Error> dealWithAlreadyExists(Exception e, HttpServletRequest request){
        Error error = buildError(e, request, CONFLICT.toString());
        return new ResponseEntity<>(error, CONFLICT);
    }


    @ExceptionHandler(value = {
            CarNotAvailableException.class
    })
    public ResponseEntity<Error> dealWithCarNotAvailable(Exception e, HttpServletRequest request){
        Error error = buildError(e, request, GONE.toString());
        return new ResponseEntity<>(error, GONE);
    }


    @ExceptionHandler(value = {
            InvalidRentalStatusException.class
    })
    public ResponseEntity<Error> dealWithInvalidRentalStatus(Exception e, HttpServletRequest request){
        Error error = buildError(e, request, NOT_FOUND.toString());
        return new ResponseEntity<>(error, NOT_FOUND);
    }


    @ExceptionHandler(value = {
            ConnectException.class
    })
    public ResponseEntity<Error> dealWithDatabaseCommunication(Exception e, HttpServletRequest request){
        Error error = buildError(e, request, SERVICE_UNAVAILABLE.toString());
        return new ResponseEntity<>(error, SERVICE_UNAVAILABLE);
    }




    private Error buildError(Exception e, HttpServletRequest request, String statusCode) {
        return Error.builder()
                .timestamp(new Date())
                .verb(request.getMethod())
                .path(request.getRequestURI())
                .statusCode(statusCode)
                .message(e.getMessage())
                .build();
    }
}

