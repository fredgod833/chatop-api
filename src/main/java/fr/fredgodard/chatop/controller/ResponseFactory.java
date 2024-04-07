package fr.fredgodard.chatop.controller;

import com.nimbusds.jose.shaded.gson.Gson;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serializable;

import static org.springframework.http.MediaType.APPLICATION_JSON;

public class ResponseFactory {

    // ThreadLocal used to have thread safety garanty.
    private static final ThreadLocal<Gson> GSON = ThreadLocal.withInitial(Gson::new);

    public static ResponseEntity<String> buildMessageResponse(HttpStatus status, final String message) {
        return buildNamedStringResponse(status, "message", message);
    }

    public static ResponseEntity<String> buildNamedObjectResponse(HttpStatus status, final String name, final Serializable bean) {
        final String serialized = GSON.get().toJson(bean);
        return ResponseEntity
                .status(status)
                .contentType(APPLICATION_JSON)
                .body("{\n" +
                        "  \""+name+"\": "+ serialized + "\n" +
                        "}");
    }

    public static ResponseEntity<String> buildNamedStringResponse(HttpStatus status, final String name, final String message) {
        return ResponseEntity
                .status(status)
                .contentType(APPLICATION_JSON)
                .body("{\n" +
                        "  \""+name+"\": \"" + message + "\"\n" +
                        "}");
    }

}
