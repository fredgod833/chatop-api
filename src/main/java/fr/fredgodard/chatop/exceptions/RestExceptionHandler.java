package fr.fredgodard.chatop.exceptions;

import fr.fredgodard.chatop.model.ApiMessage;
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


    @ExceptionHandler(ClientNotFoundException.class)
    protected ResponseEntity<ApiMessage> handleClientNotFoundException(
            ClientNotFoundException e) {
        ApiMessage apiResponse = new ApiMessage();
        apiResponse.setMessage(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(APPLICATION_JSON)
                .body(apiResponse);
    }

    @ExceptionHandler(ClientException.class)
    protected ResponseEntity<ApiMessage> handleClientException(
            ClientException e) {
        ApiMessage apiResponse = new ApiMessage();
        apiResponse.setMessage(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(APPLICATION_JSON)
                .body(apiResponse);
    }

    @ExceptionHandler(AuthException.class)
    protected ResponseEntity<ApiMessage> handleAuthException(
            AuthException e) {
        ApiMessage apiResponse = new ApiMessage();
        apiResponse.setMessage(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.UNAUTHORIZED)
                .contentType(APPLICATION_JSON)
                .body(apiResponse);
    }

    @ExceptionHandler(RentalException.class)
    protected ResponseEntity<ApiMessage> handleRentalException(RentalException e) {
        ApiMessage apiResponse = new ApiMessage();
        apiResponse.setMessage(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(APPLICATION_JSON)
                .body(apiResponse);
    }

    @ExceptionHandler(RentalNotFoundException.class)
    protected ResponseEntity<ApiMessage> handleRentalNotFoundException(RentalNotFoundException e) {
        ApiMessage apiResponse = new ApiMessage();
        apiResponse.setMessage(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .contentType(APPLICATION_JSON)
                .body(apiResponse);
    }

    @ExceptionHandler(MessageException.class)
    protected ResponseEntity<ApiMessage> handleMessageException(MessageException e) {
        ApiMessage apiResponse = new ApiMessage();
        apiResponse.setMessage(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(APPLICATION_JSON)
                .body(apiResponse);
    }

    @ExceptionHandler(FileStorageException.class)
    protected ResponseEntity<ApiMessage> handleStorageException(FileStorageException e) {
        ApiMessage apiResponse = new ApiMessage();
        apiResponse.setMessage(e.getMessage());
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .contentType(APPLICATION_JSON)
                .body(apiResponse);
    }

}
