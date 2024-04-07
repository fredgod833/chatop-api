package fr.fredgodard.chatop.model;

import lombok.Data;

import java.io.Serializable;
import java.util.ArrayList;

@Data
public class RentalList implements Serializable {

    private ArrayList<Rental> rentals = new ArrayList<>();

}
