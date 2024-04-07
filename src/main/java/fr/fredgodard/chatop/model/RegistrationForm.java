package fr.fredgodard.chatop.model;

import lombok.Data;

import java.io.Serializable;

@Data
public class RegistrationForm implements Serializable {

    private String name;

    private String email;

    private String password;

}
