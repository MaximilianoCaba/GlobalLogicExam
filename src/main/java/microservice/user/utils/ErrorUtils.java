package microservice.user.utils;

import microservice.user.exeption.response.GenericError;
import microservice.user.exeption.response.GenericErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.sql.Timestamp;
import java.util.List;
import java.util.stream.Collectors;

public class ErrorUtils {

  public static GenericErrorResponse buildError(String message, HttpStatus codeError) {
    GenericErrorResponse genericErrors = new GenericErrorResponse();
    GenericError genericError = new GenericError();
    Timestamp timestamp = new Timestamp(System.currentTimeMillis());
    genericError.setTimestamp(timestamp);
    genericError.setCodigo(codeError.value());
    genericError.setDetail(message);
    genericErrors.getErrors().add(genericError);
    return genericErrors;
  }

  public static GenericErrorResponse buildMultiplesErrorByMethodArgumentNotValidException(
      MethodArgumentNotValidException exception,
      HttpStatus codeError
  ) {
    return exception.getBindingResult().getFieldErrors().stream()
        .map(fieldError -> {
          GenericError genericError = new GenericError();
          genericError.setTimestamp(new Timestamp(System.currentTimeMillis()));
          genericError.setCodigo(codeError.value());
          genericError.setDetail(fieldError.getDefaultMessage());
          return genericError;
        })
        .collect(Collectors.collectingAndThen(Collectors.toList(),
            genericErrorsList -> {
              GenericErrorResponse genericErrors = new GenericErrorResponse();
              genericErrors.setErrors(genericErrorsList);
              return genericErrors;
            }));
  }
}
