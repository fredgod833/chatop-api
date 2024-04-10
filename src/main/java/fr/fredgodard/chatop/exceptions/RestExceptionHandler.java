package fr.fredgodard.chatop.exceptions;

import fr.fredgodard.chatop.model.ApiResponse;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import static org.springframework.http.MediaType.APPLICATION_JSON;


@Order(Ordered.HIGHEST_PRECEDENCE)
@ControllerAdvice
public class RestExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(ClientException.class)
    protected ResponseEntity<ApiResponse> handleClientException(
            ClientException e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(APPLICATION_JSON)
                .body(apiResponse);
    }

    @ExceptionHandler(AuthException.class)
    protected ResponseEntity<ApiResponse> handleAuthException(
            AuthException e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(APPLICATION_JSON)
                .body(apiResponse);
    }

    @ExceptionHandler(RentalException.class)
    protected ResponseEntity<ApiResponse> handleRentalException(RentalException e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(APPLICATION_JSON)
                .body(apiResponse);
    }

    @ExceptionHandler(MessageException.class)
    protected ResponseEntity<ApiResponse> handleMessageException(MessageException e) {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setMessage(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(APPLICATION_JSON)
                .body(apiResponse);
    }

}
