package fr.fredgodard.chatop.service;

import fr.fredgodard.chatop.repository.entities.UserEntity;
import fr.fredgodard.chatop.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
public class UserDetailsService implements org.springframework.security.core.userdetails.UserDetailsService {

    @Autowired
    private UsersRepository usersRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<UserEntity> userEntities = usersRepository.findUserByEmail(username);
        if (userEntities.size() == 1) {
            return chatopUser2UserDetails(userEntities.get(0));
        }
        throw new UsernameNotFoundException("Unknown User !! " + username);
    }

    private UserDetails chatopUser2UserDetails(UserEntity userEntity) {
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        return new User(userEntity.getEmail(), userEntity.getPassword(), authorities);
    }

}
