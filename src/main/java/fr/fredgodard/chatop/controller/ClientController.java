package fr.fredgodard.chatop.controller;

import fr.fredgodard.chatop.configuration.jwt.JwtService;
import fr.fredgodard.chatop.exceptions.ClientNotFoundException;
import fr.fredgodard.chatop.model.ApiMessage;
import fr.fredgodard.chatop.model.Client;
import fr.fredgodard.chatop.service.ChatopUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@Tag(name="User", description="Get user informations.")
public class ClientController {
    private final ChatopUserService chatopUserService;

    public ClientController(JwtService jwtService, ChatopUserService chatopUserService) {
        this.chatopUserService = chatopUserService;
    }

    @GetMapping(value ="/api/user/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary="Get user profile by id.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "Return the user informations.",
                    content = @Content(schema = @Schema(implementation = Client.class))
            ),
            @ApiResponse(responseCode = "401",
                    description = "Invalid connection, token expired, etc..",
                    content = @Content(schema = @Schema(implementation = ApiMessage.class))
            )})
    @SecurityRequirement(name = "Bearer JWT Authentication")
    public ResponseEntity<Client> getUser(@PathVariable("id") final Integer id) throws ClientNotFoundException {
        Client client = chatopUserService.getUserById(id);
        return ResponseEntity.status(OK).contentType(APPLICATION_JSON).body(client);
    }

    @DeleteMapping("api/user/{id}")
    @Operation(summary="Delete user by id.")
    @SecurityRequirement(name = "Bearer JWT Authentication")
    public void deleteUserById(@PathVariable("id") final Integer id) {
        chatopUserService.deleteUserById(id);
    }

}