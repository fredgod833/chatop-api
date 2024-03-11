package fr.fredgodard.chatop.repository;

import fr.fredgodard.chatop.repository.entities.RentalEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RentalsRepository extends CrudRepository<RentalEntity, Integer>, JpaRepository<RentalEntity, Integer> {

    List<RentalEntity> findByOwnerId(Integer id);

}
