package fr.fredgodard.chatop.service;

import fr.fredgodard.chatop.exceptions.AuthException;
import fr.fredgodard.chatop.exceptions.ClientException;
import fr.fredgodard.chatop.exceptions.ClientNotFoundException;
import fr.fredgodard.chatop.repository.entities.UserEntity;
import fr.fredgodard.chatop.repository.UsersRepository;
import fr.fredgodard.chatop.model.Client;
import fr.fredgodard.chatop.model.Credentials;
import fr.fredgodard.chatop.model.RegistrationForm;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Data
@Service
@Slf4j
public class ChatopUserService {

    private UsersRepository usersRepository;

    private PasswordEncoder encoder;
    public ChatopUserService(UsersRepository usersRepository, PasswordEncoder encoder) {
        this.usersRepository = usersRepository;
        this.encoder = encoder;
    }

    private static Client entity2Bean(UserEntity entity) {
        if (entity == null) {
            return null;
        }
        Client result = new Client();
        result.setId(entity.getId());
        result.setName(entity.getName());
        result.setEmail(entity.getEmail());
        result.setCreatedAt(entity.getCreatedAt());
        result.setUpdatedAt(entity.getCreatedAt());
        return result;
    }

    private UserEntity registerForm2Entity(RegistrationForm registerInfo) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(registerInfo.getName());
        userEntity.setEmail(registerInfo.getEmail());
        userEntity.setPassword(encoder.encode(registerInfo.getPassword()));
        return userEntity;
    }

    public Client getUserById(final Integer id) throws ClientNotFoundException {
        Optional<UserEntity> userEntity = usersRepository.findById(id);
        if (! userEntity.isPresent()) {
            throw new ClientNotFoundException("User not found");
        }
        return userEntity.map(ChatopUserService::entity2Bean).get();
    }

    public void deleteUserById(@PathVariable("id") final Integer id) {
        usersRepository.deleteById(id);
    }

    public Client register(RegistrationForm registerInfo) throws ClientException {
        try {
            UserEntity userEntity = registerForm2Entity(registerInfo);
            UserEntity savedEntity = usersRepository.save(userEntity);
            return entity2Bean(savedEntity);
        } catch (Exception e) {
            throw new ClientException("This account already exists, please log in.",e) ;
        }
    }

    public Client loadConnectedUser(final Authentication auth) throws AuthException {
        if (auth == null || auth.getName() == null) {
            throw new AuthException("Invalid user");
        }
        List<UserEntity> userEntities = usersRepository.findUserByEmail(auth.getName());
        if (userEntities.size() == 1) {
            return entity2Bean(userEntities.get(0));
        }
        throw new AuthException("Current user unknown, please log in.");
    }

    public Client loadWithCredentials(final Credentials userCredentials) throws AuthException {
        List<UserEntity> userEntities = usersRepository.findUserByEmail(userCredentials.getEmail());
        if (userEntities.size() == 1) {
            UserEntity userEntity = userEntities.get(0);
            if (userEntity == null || !encoder.matches(userCredentials.getPassword(), userEntity.getPassword())) {
                throw new AuthException("User not found or invalid credentials.");
            }
            return entity2Bean(userEntity);
        }
        throw new AuthException("User not found or invalid credentials.");
    }

}
