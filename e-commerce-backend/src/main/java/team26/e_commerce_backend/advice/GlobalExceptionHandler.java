package team26.e_commerce_backend.advice;

import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import team26.e_commerce_backend.dto.response.StockErrorResponse;
import team26.e_commerce_backend.exception.InsufficientStockException;

@RestControllerAdvice
public class GlobalExceptionHandler {
  @ExceptionHandler(InsufficientStockException.class)
  public ResponseEntity<List<StockErrorResponse>> handleInsufficientStockException(
      InsufficientStockException ex) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getStockErrors());
  }
}
