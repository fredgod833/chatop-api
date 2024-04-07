package fr.fredgodard.chatop.controller;

import fr.fredgodard.chatop.configuration.jwt.JwtService;
import fr.fredgodard.chatop.model.Client;
import fr.fredgodard.chatop.service.ChatopUserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class ClientController {

    private final ChatopUserService chatopUserService;

    public ClientController(JwtService jwtService, ChatopUserService chatopUserService) {
        this.chatopUserService = chatopUserService;
    }

    @GetMapping(value ="/api/user/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Client> getUser(@PathVariable("id") final Integer id) {
        Optional<Client> user = chatopUserService.getUserById(id);
        return user.map(chatopUser -> ResponseEntity.status(OK).body(chatopUser)).orElseGet(() -> ResponseEntity.status(UNAUTHORIZED).contentType(APPLICATION_JSON).body(null));
    }

    @GetMapping("api/user/delete/{id}")
    public void deleteUserById(@PathVariable("id") final Integer id) {
        chatopUserService.deleteUserById(id);
    }

}