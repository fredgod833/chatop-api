package fr.fredgodard.chatop.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Data
public class RentalList implements Serializable {

    private ArrayList<Rental> rentals = new ArrayList<>();

    public void addRental(Rental rental) {
        rentals.add(rental);
    }

    public void addRentals(Collection<Rental> rentals) {
        this.rentals.addAll(rentals);
    }

}
