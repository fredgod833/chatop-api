package fr.fredgodard.chatop.service;

import fr.fredgodard.chatop.model.ChatopUserEntity;
import fr.fredgodard.chatop.repository.ChatopUsersRepository;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.Optional;

@Data
@Service
public class ChatopUserEntityService {

    @Autowired
    private ChatopUsersRepository usersRepository;


    public Optional<ChatopUserEntity> getUserById(final Integer id) {
        return usersRepository.findById(id);
    }

    public void deleteUserById(@PathVariable("id") final Integer id) {
        usersRepository.deleteById(id);
    }

    public ChatopUserEntity saveUser(final ChatopUserEntity chatopUserEntity) {
        return usersRepository.save(chatopUserEntity);
    }

    public Optional<ChatopUserEntity> getUserByEmail(final String email) {
        List<ChatopUserEntity> chatopUserEntities = usersRepository.findUserByEmail(email);
        if (chatopUserEntities.size() == 1) {
            return Optional.of(chatopUserEntities.get(0));
        }
        return Optional.empty();
    }

}
