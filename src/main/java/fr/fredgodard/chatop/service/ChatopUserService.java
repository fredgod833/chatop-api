package fr.fredgodard.chatop.service;

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
        result.setCreated_at(entity.getCreated_at());
        result.setUpdated_at(entity.getCreated_at());
        return result;
    }

    private UserEntity registerForm2Entity(RegistrationForm registerInfo) {
        UserEntity userEntity = new UserEntity();
        userEntity.setName(registerInfo.getName());
        userEntity.setEmail(registerInfo.getEmail());
        userEntity.setPassword(encoder.encode(registerInfo.getPassword()));
        return userEntity;
    }



    public Optional<Client> getUserById(final Integer id) {
        Optional<UserEntity> userEntity = usersRepository.findById(id);
        return userEntity.map(ChatopUserService::entity2Bean);
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

    public Client loadConnectedUser(final Authentication auth) throws ClientException {
        List<UserEntity> userEntities = usersRepository.findUserByEmail(auth.getName());
        if (userEntities.size() == 1) {
            return entity2Bean(userEntities.get(0));
        }
        throw new ClientException("Unknown client, please log in.");
    }

    public Optional<Client> loadWithCredentials(final Credentials userCredentials) {
        // TODO : ne pas retourner un Optional mais déclencher une exception userntofound
        List<UserEntity> userEntities = usersRepository.findUserByEmail(userCredentials.getEmail());
        if (userEntities.size() == 1) {
            UserEntity userEntity = userEntities.get(0);
            if (userEntity == null || !encoder.matches(userCredentials.getPassword(), userEntity.getPassword())) {
                return Optional.empty();
            }
            return Optional.of(entity2Bean(userEntity));
        }
        return Optional.empty();
    }

}
