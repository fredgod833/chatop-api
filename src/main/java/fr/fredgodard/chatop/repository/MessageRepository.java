package fr.fredgodard.chatop.repository;

import fr.fredgodard.chatop.repository.entities.MessageEntity;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MessageRepository extends CrudRepository<MessageEntity, Integer> {

}
