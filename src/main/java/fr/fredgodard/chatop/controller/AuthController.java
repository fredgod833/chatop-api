package fr.fredgodard.chatop.controller;

import fr.fredgodard.chatop.configuration.jwt.JwtService;
import fr.fredgodard.chatop.exceptions.AuthException;
import fr.fredgodard.chatop.exceptions.ClientException;
import fr.fredgodard.chatop.model.*;
import fr.fredgodard.chatop.service.ChatopUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.apache.logging.log4j.util.Strings.isBlank;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Tag(name="Authentication", description="Register, Login and get current user information's")
public class AuthController {

    private final ChatopUserService chatopUserService;
    private final JwtService jwtService;

    public AuthController(JwtService jwtService, ChatopUserService chatopUserService) {
        this.jwtService = jwtService;
        this.chatopUserService = chatopUserService;
    }

    private ResponseEntity<Token> buildTokenResponseEntity(Client client) {
        final Token token = jwtService.generateAccessToken(client);
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(token);
    }

    @PostMapping(value ="/api/auth/login", consumes = APPLICATION_JSON_VALUE, produces= APPLICATION_JSON_VALUE)
    @Operation(summary="Log in Client with credentials.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                         description = "Log in successfull, bearer token returned.",
                         content = @Content(schema = @Schema(implementation = Token.class))
                        ),
            @ApiResponse(responseCode = "401",
                         description = "Invalid login, error message.",
                         content = @Content(schema = @Schema(implementation = ApiMessage.class))
            )})
    public ResponseEntity<Token> login(@RequestBody Credentials credentials) throws AuthException {
        Client  client = chatopUserService.loadWithCredentials(credentials);
        return buildTokenResponseEntity(client);
    }

    @PostMapping(value = "/api/auth/register", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary="Register new user.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Registration successfull, bearer token returned.",
                    content = @Content(schema = @Schema(implementation = Token.class))
            ),
            @ApiResponse(responseCode = "400",
                    description = "Invalid form, bad request, user already exists. The reason is returned in message.",
                    content = @Content(schema = @Schema(implementation = ApiMessage.class))
            )})
    public ResponseEntity<Token> registerUser(@RequestBody RegistrationForm registrationForm) throws ClientException {
        if (registrationForm == null) {
            throw new ClientException("Please, fill in the form.");
        }
        if (isBlank(registrationForm.getName())) {
            throw new ClientException("Name field is mandatory.");
        }
        if (isBlank(registrationForm.getEmail())) {
            throw new ClientException("Email field is mandatory.");
        }
        if (isBlank(registrationForm.getPassword())) {
            throw new ClientException("Password field is mandatory.");
        }
        Client client = chatopUserService.register(registrationForm);
        return buildTokenResponseEntity(client);
    }

    @GetMapping(value ="/api/auth/me", produces = APPLICATION_JSON_VALUE)
    @Operation(summary="Get the connected user informations.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Return the connected user informations.",
                    content = @Content(schema = @Schema(implementation = Client.class))
            ),
            @ApiResponse(responseCode = "401",
                    description = "Invalid connection, token expired, etc..",
                    content = @Content(schema = @Schema(implementation = ApiMessage.class))
            )})
    @SecurityRequirement(name = "Bearer JWT Authentication")
    public ResponseEntity<Client> getConnectedUser(Authentication auth) throws AuthException {
            Client user = chatopUserService.loadConnectedUser(auth);
            return ResponseEntity.status(OK).body(user);
    }

}