package fr.fredgodard.chatop.repository;

import fr.fredgodard.chatop.repository.entities.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UsersRepository extends CrudRepository<UserEntity, Integer>, JpaRepository<UserEntity, Integer> {

    List<UserEntity> findUserByName(final String name);

    List<UserEntity> findUserByEmail(String email);

}
