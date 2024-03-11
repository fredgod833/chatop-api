package fr.fredgodard.chatop.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;

@Data
public class Message implements Serializable {

    @JsonProperty("owner_id")
    private Integer ownerId;

    @JsonProperty("rental_id")
    private Integer rentalId;

    private String message;
}
