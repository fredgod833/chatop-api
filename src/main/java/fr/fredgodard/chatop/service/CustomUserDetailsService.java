package fr.fredgodard.chatop.service;

import fr.fredgodard.chatop.model.ChatopUserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private ChatopUserEntityService chatopUserEntityService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<ChatopUserEntity> chatopUserEntity = chatopUserEntityService.getUserByEmail(username);
        if (chatopUserEntity.isPresent()) {
            return chatopUser2UserDetails(chatopUserEntity.get());
        }
        throw new UsernameNotFoundException("Invalid user " + username);
    }

    private UserDetails chatopUser2UserDetails(ChatopUserEntity chatopUserEntity) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new User(chatopUserEntity.getEmail(), chatopUserEntity.getPassword(), authorities);
    }

}
