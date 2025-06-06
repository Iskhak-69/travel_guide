package alatoo.travel_guide.exceptions;

import alatoo.travel_guide.dto.ErrorDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorDto> handleApiException(ApiException e){
        return ResponseEntity.status(e.getStatusCode()).body(new ErrorDto(e.getMessage(), e.getStatusCode().value()));
    }
}