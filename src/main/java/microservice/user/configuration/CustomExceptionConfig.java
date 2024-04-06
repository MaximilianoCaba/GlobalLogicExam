package microservice.user.configuration;

import microservice.user.exeption.SignUpException;
import microservice.user.exeption.UserNotFoundException;
import microservice.user.exeption.response.GenericErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import static microservice.user.utils.ErrorUtils.buildError;
import static microservice.user.utils.ErrorUtils.buildMultiplesErrorByMethodArgumentNotValidException;

@ControllerAdvice
class CustomExceptionConfig {

  @ExceptionHandler(SignUpException.class)
  public ResponseEntity<GenericErrorResponse> exception(SignUpException e) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(buildError(e.getMessage(), HttpStatus.BAD_REQUEST));
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<GenericErrorResponse> exception(MethodArgumentNotValidException e) {
    return ResponseEntity
        .status(HttpStatus.BAD_REQUEST)
        .body(buildMultiplesErrorByMethodArgumentNotValidException(e, HttpStatus.BAD_REQUEST));
  }

  @ExceptionHandler(UserNotFoundException.class)
  public ResponseEntity<GenericErrorResponse> exception(UserNotFoundException e) {
    return ResponseEntity
        .status(HttpStatus.NOT_FOUND)
        .body(buildError(e.getMessage(), HttpStatus.NOT_FOUND));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<GenericErrorResponse> exception(Exception e) {
    return ResponseEntity
        .status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(buildError(e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR));
  }

}