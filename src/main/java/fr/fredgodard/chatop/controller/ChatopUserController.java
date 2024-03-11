package fr.fredgodard.chatop.controller;

import fr.fredgodard.chatop.model.ChatopUserEntity;
import fr.fredgodard.chatop.service.ChatopUserEntityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
public class ChatopUserController {


    @Autowired
    private ChatopUserEntityService chatopUserEntityService;

    @GetMapping("/user/byid/{id}")
    public ChatopUserEntity getUserById(@PathVariable("id") final Integer id) {
        Optional<ChatopUserEntity> user = chatopUserEntityService.getUserById(id);
        if (user.isPresent()) {
            return user.get();
        } else {
            //TODO : Service Exception ? 404 ? WhatElse ?
            return null;
        }
    }

    @GetMapping("/user/byemail/{email}")
    public ChatopUserEntity getUserByEmail(@PathVariable("email") final String email) {
        Optional<ChatopUserEntity> user = chatopUserEntityService.getUserByEmail(email);
        if (user.isPresent()) {
            return user.get();
        } else {
            //TODO : Service Exception ? 404 ? WhatElse ?
            return null;
        }
    }

    @GetMapping("/user/delete/{id}")
    public void deleteUserById(@PathVariable("id") final Integer id) {
        chatopUserEntityService.deleteUserById(id);
    }

    @PostMapping("/user/create")
    public ChatopUserEntity createUser(@RequestBody final ChatopUserEntity chatopUserEntity) {
        PasswordEncoder encoder = new BCryptPasswordEncoder();
        chatopUserEntity.setPassword(encoder.encode(chatopUserEntity.getPassword()));
        return chatopUserEntityService.saveUser(chatopUserEntity);
    }

}
