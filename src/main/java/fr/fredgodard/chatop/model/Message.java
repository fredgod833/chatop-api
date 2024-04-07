package fr.fredgodard.chatop.model;

import lombok.Data;

@Data
public class Message {

    private Integer owner_id;

    private Integer rental_id;

    private String message;
}
