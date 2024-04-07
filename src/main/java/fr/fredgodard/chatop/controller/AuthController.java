package fr.fredgodard.chatop.controller;

import fr.fredgodard.chatop.configuration.jwt.JwtService;
import fr.fredgodard.chatop.model.Client;
import fr.fredgodard.chatop.model.Credentials;
import fr.fredgodard.chatop.model.RegistrationForm;
import fr.fredgodard.chatop.service.ClientException;
import fr.fredgodard.chatop.service.ChatopUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

import static fr.fredgodard.chatop.controller.ResponseFactory.buildMessageResponse;
import static fr.fredgodard.chatop.controller.ResponseFactory.buildNamedStringResponse;
import static org.apache.logging.log4j.util.Strings.isBlank;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
public class AuthController {

    private final ChatopUserService chatopUserService;
    private final JwtService jwtService;

    private ResponseEntity<String> buildTokenResponse(HttpStatus status, Client user) {
        final String token = jwtService.generateAccessToken(user);
        return buildNamedStringResponse(status, "token", token);
    }

    public AuthController(JwtService jwtService, ChatopUserService chatopUserService) {
        this.jwtService = jwtService;
        this.chatopUserService = chatopUserService;
    }

    @PostMapping(value ="/api/auth/login", consumes = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> login(@RequestBody Credentials credentials) {
        Optional<Client>  user = chatopUserService.loadWithCredentials(credentials);
        return user
                .map(chatopUser -> buildTokenResponse(OK, chatopUser))
                .orElseGet(
                        () -> buildMessageResponse(UNAUTHORIZED, "Invalid login or password")
                );
    }

    @PostMapping(value = "/api/auth/register", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> registerUser(@RequestBody RegistrationForm registrationForm) {
        // TODO voir les validateurs
        if (registrationForm == null) {
            return buildMessageResponse(BAD_REQUEST,"Fill in the form.");
        }
        if (isBlank(registrationForm.getName())) {
            return buildMessageResponse(BAD_REQUEST,"Name is mandatory.");
        }
        if (isBlank(registrationForm.getEmail())) {
            return buildMessageResponse(BAD_REQUEST,"Email is mandatory.");
        }
        if (isBlank(registrationForm.getPassword())) {
            return buildMessageResponse(BAD_REQUEST,"Password is mandatory.");
        }

        try {
            Client registeredUser = chatopUserService.register(registrationForm);
            return buildTokenResponse(CREATED, registeredUser);
        } catch (ClientException e) {
            return buildMessageResponse(NOT_ACCEPTABLE, e.getMessage());
        }

    }

    @GetMapping(value ="/api/auth/me", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<Client> getConnectedUser(Authentication auth) {
        try {
            Client user = chatopUserService.loadConnectedUser(auth);
            return ResponseEntity.status(OK).body(user);
        } catch (ClientException e) {
            return ResponseEntity.status(UNAUTHORIZED).contentType(APPLICATION_JSON).body(null);
        }
    }

}