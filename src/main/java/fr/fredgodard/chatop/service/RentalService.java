package fr.fredgodard.chatop.service;

import fr.fredgodard.chatop.exceptions.RentalException;
import fr.fredgodard.chatop.model.Rental;
import fr.fredgodard.chatop.model.Client;
import fr.fredgodard.chatop.repository.RentalsRepository;
import fr.fredgodard.chatop.repository.entities.RentalEntity;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class RentalService {

    private final RentalsRepository rentalRepository;

    public RentalService(RentalsRepository rentalRepository) {
        this.rentalRepository = rentalRepository;
    }

    private String getRequestPath()
    {
        URL url;
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();
            url = new URL("http",request.getServerName(),request.getServerPort(),request.getContextPath());
            return url +"/pictures/";
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

    }

    public Rental saveRental(Rental rental) {
        RentalEntity rentalEntity = Rental2Entity(rental);
        RentalEntity saved = rentalRepository.save(rentalEntity);
        return entity2Rental(saved, getRequestPath());
    }

    public Rental getRental(Integer rentalId) throws RentalException {
        Optional<RentalEntity> rentalEntity = rentalRepository.findById(rentalId);
        if (rentalEntity.isEmpty()) {
            throw new RentalException("Rental not found ! ", null);
        }
        return entity2Rental(rentalEntity.get(), getRequestPath());
    }

    public ArrayList<Rental> listRentals() {
        List<RentalEntity> rentals = rentalRepository.findAll();
        final String requestPath = getRequestPath();
        ArrayList<Rental> result = new ArrayList<>();
        rentals.forEach(rental -> result.add(entity2Rental(rental, requestPath)));
        return result;
    }

    public ArrayList<Rental> listRentals(Client owner) {
        List<RentalEntity> rentals = rentalRepository.findByOwnerId(owner.getId());
        final String requestPath = getRequestPath();
        ArrayList<Rental> result = new ArrayList<>();
        rentals.forEach(rental -> result.add(entity2Rental(rental, requestPath)));
        return result;
    }

    private Rental entity2Rental(RentalEntity rental, final String contextPath) {
        Rental result = new Rental();
        result.setId(rental.getId());
        result.setName(rental.getName());
        result.setSurface(rental.getSurface());
        result.setPrice(rental.getPrice());
        result.setDescription(rental.getDescription());
        result.setPicture(contextPath+rental.getPicture());
        result.setOwner_id(rental.getOwnerId());
        result.setCreated_at(rental.getCreated_at());
        result.setUpdated_at(rental.getUpdated_at());
        return result;
    }

    private RentalEntity Rental2Entity(Rental rental) {
        RentalEntity result = new RentalEntity();
        result.setId(rental.getId());
        result.setName(rental.getName());
        result.setSurface(rental.getSurface());
        result.setPrice(rental.getPrice());
        result.setDescription(rental.getDescription());
        result.setPicture(rental.getPicture());
        result.setOwnerId(rental.getOwner_id());
        return result;
    }

    public Rental updateRental(Rental rental) throws RentalException {
        Optional<RentalEntity> searchedEntity = rentalRepository.findById(rental.getId());
        if (searchedEntity.isEmpty()) {
            throw new RentalException("Rental not found ! ", null);
        }
        RentalEntity entity = searchedEntity.get();
        entity.setName(rental.getName());
        entity.setSurface(rental.getSurface());
        entity.setPrice(rental.getPrice());
        entity.setDescription(rental.getDescription());
        RentalEntity saved = rentalRepository.save(entity);
        return entity2Rental(saved, getRequestPath());
    }
}
