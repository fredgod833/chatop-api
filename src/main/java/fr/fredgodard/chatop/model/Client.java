package fr.fredgodard.chatop.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class Client implements Serializable {

    private Integer id;

    private String name;

    private String email;

    private LocalDateTime created_at;

    private LocalDateTime updated_at;

}
