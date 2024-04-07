package fr.fredgodard.chatop.controller;

import fr.fredgodard.chatop.configuration.jwt.JwtService;
import fr.fredgodard.chatop.model.Message;
import fr.fredgodard.chatop.service.RentalException;
import fr.fredgodard.chatop.service.MessageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static fr.fredgodard.chatop.controller.ResponseFactory.buildMessageResponse;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class MessageController {

    private final MessageService messageService;

    public MessageController(JwtService jwtService, MessageService messageService) {
        this.messageService = messageService;
    }

    @PostMapping(value ="/api/messages", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> saveMessage(@RequestBody() final Message message) {
        try {
            messageService.saveMessage(message);
        } catch (RentalException e) {
            return buildMessageResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
        return buildMessageResponse(OK, "Message send with success");
    }

}