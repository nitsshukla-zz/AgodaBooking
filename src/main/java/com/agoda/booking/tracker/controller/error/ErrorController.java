package com.agoda.booking.tracker.controller.error;

import com.agoda.booking.tracker.exception.DuplicateBookingPostedException;
import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
@Slf4j
public class ErrorController  extends ResponseEntityExceptionHandler  {
  public static final Gson gson = new Gson();
  @Override
  protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex,
                                                                final HttpHeaders headers,
                                                                final HttpStatus status,
                                                                final WebRequest request) {
    logger.error("Validation error: ", ex);

    ResponseEntity<Object> responseEntity = super.handleMethodArgumentNotValid(ex, headers, status, request);

    return ResponseEntity.badRequest()
        .headers(responseEntity.getHeaders())
        .body(gson.toJson(ex.getBindingResult().getAllErrors()));
  }
  @ExceptionHandler(RuntimeException.class)
  public ResponseEntity<String> handleGenericException(MethodArgumentNotValidException ex, WebRequest request) {
    logger.error("invalid exception: " + ex);
    return ResponseEntity.internalServerError().body(ex.getMessage());
  }
  @ExceptionHandler(DuplicateBookingPostedException.class)
  public ResponseEntity<String> handleDupBookingIdException(DuplicateBookingPostedException ex, WebRequest request) {
    logger.error("invalid exception: " + ex);
    return ResponseEntity.unprocessableEntity().body(ex.getMessage());
  }
}
