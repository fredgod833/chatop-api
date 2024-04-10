package fr.fredgodard.chatop.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class Message implements Serializable {

    private Integer owner_id;

    private Integer rental_id;

    private String message;
}
