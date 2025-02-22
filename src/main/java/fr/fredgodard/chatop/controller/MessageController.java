package fr.fredgodard.chatop.controller;

import fr.fredgodard.chatop.configuration.jwt.JwtService;
import fr.fredgodard.chatop.exceptions.MessageException;
import fr.fredgodard.chatop.model.ApiResponse;
import fr.fredgodard.chatop.model.Message;
import fr.fredgodard.chatop.service.MessageService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Tag(name="Message", description="Send In-mails.")
public class MessageController {

    private final MessageService messageService;

    public MessageController(JwtService jwtService, MessageService messageService) {
        this.messageService = messageService;
    }

    @Operation(summary="Post new inMail.")
    @PostMapping(value ="/api/messages", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<ApiResponse> saveMessage(@RequestBody() final Message message) throws MessageException {
        messageService.saveMessage(message);
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(new ApiResponse("Your message has been sent"));
    }

}