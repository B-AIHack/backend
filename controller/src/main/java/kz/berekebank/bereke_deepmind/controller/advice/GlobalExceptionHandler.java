package kz.berekebank.bereke_deepmind.controller.advice;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@Slf4j
@RestControllerAdvice
@RequiredArgsConstructor
public class GlobalExceptionHandler {

  @ExceptionHandler(value = {RuntimeException.class})
  @ResponseStatus(HttpStatus.BAD_REQUEST)
  @ResponseBody
  public ResponseEntity<ControllerExceptionResponse> handleValidationFailed(RuntimeException ex,
                                                                            WebRequest ignored) {
    log.error("S5V5poYz :: RuntimeException failed {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ControllerExceptionResponse(ex.getMessage()));
  }

  @ExceptionHandler(Exception.class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  public ResponseEntity<ControllerExceptionResponse> handleException(Exception ex) {
    log.warn("ksFy5qtw :: Exception error: ", ex);
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ControllerExceptionResponse(ex.getMessage()));
  }

}
