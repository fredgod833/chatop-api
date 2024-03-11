package fr.fredgodard.chatop.repository;

import fr.fredgodard.chatop.model.ChatopUserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatopUsersRepository extends CrudRepository<ChatopUserEntity, Integer>, JpaRepository<ChatopUserEntity, Integer> {

    List<ChatopUserEntity> findUserByName(final String name);

    List<ChatopUserEntity> findUserByEmail(String email);
}
