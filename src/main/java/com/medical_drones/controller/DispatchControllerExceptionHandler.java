package com.medical_drones.controller;

import com.medical_drones.exception.DroneAlreadyExistsException;
import com.medical_drones.exception.DroneNotFoundException;
import com.medical_drones.exception.DroneUnableToBeLoadedException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = DispatcherController.class)
public class DispatchControllerExceptionHandler {

    @ExceptionHandler(DroneNotFoundException.class)
    public final ResponseEntity<String> droneNotFoundException(DroneNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler(DroneAlreadyExistsException.class)
    public final ResponseEntity<String> droneAlreadyExists(DroneAlreadyExistsException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(DroneUnableToBeLoadedException.class)
    public final ResponseEntity<String> droneUnableToBeLoaded(DroneUnableToBeLoadedException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    @ExceptionHandler(ConstraintViolationException.class)
    public final ResponseEntity<String> entityUnableToBeSaved(ConstraintViolationException ex) {
        StringBuilder sb = new StringBuilder();
        for (ConstraintViolation<?> constraintViolation : ex.getConstraintViolations()) {
            sb.append(constraintViolation.getMessage()).append("\n");
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(sb.toString());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public final ResponseEntity<String> entityUnableToBeSaved(HttpMessageNotReadableException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }
}
