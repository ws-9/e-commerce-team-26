package team26.e_commerce_backend.exception;

import java.util.List;
import team26.e_commerce_backend.dto.response.StockErrorResponse;

public class InsufficientStockException extends RuntimeException {
  private final List<StockErrorResponse> stockErrors;

  public InsufficientStockException(List<StockErrorResponse> stockErrors) {
    super("Insufficient stock for one or more products");
    this.stockErrors = stockErrors;
  }

  public List<StockErrorResponse> getStockErrors() {
    return stockErrors;
  }
}
