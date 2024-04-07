package fr.fredgodard.chatop.controller;

import com.nimbusds.jose.shaded.gson.Gson;
import fr.fredgodard.chatop.model.Rental;
import fr.fredgodard.chatop.model.Client;
import fr.fredgodard.chatop.service.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Optional;

import static fr.fredgodard.chatop.controller.ResponseFactory.buildMessageResponse;
import static fr.fredgodard.chatop.controller.ResponseFactory.buildNamedObjectResponse;
import static org.springframework.http.MediaType.*;

@RestController
public class RentalController {

    private final FileStorageService fileStorageService;

    private final RentalService rentalService;

    private final ChatopUserService chatopUserService;


    public RentalController(FileStorageService fileStorageService, RentalService rentalService, ChatopUserService chatopUserService) {
        this.rentalService = rentalService;
        this.fileStorageService = fileStorageService;
        this.chatopUserService = chatopUserService;
    }

    @PostMapping(value = "/api/rentals", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> createRental(
                             Authentication auth,
                             @RequestParam("picture") MultipartFile picture,
                             @RequestParam("name") String name,
                             @RequestParam("surface") String surface,
                             @RequestParam("price") String price,
                             @RequestParam("description") String description) {
        Rental rental = new Rental();
        rental.setName(name);
        rental.setSurface(BigDecimal.valueOf(Double.parseDouble(surface)));
        rental.setPrice(BigDecimal.valueOf(Double.parseDouble(price)));
        rental.setDescription(description);
        // récupération du user pour vérifier son existence et récup id
        try {
            Client client = chatopUserService.loadConnectedUser(auth);
            rental.setOwner_id(client.getId());
        } catch (ClientException e) {
            return buildMessageResponse(HttpStatus.BAD_REQUEST,e.getMessage());
        }

        // définition du chemin (relatif) de stockage de l'image.
        final String filePath = String.format("%1$s/%2$s/%3$s",rental.getOwner_id(),rental.getId(), picture.getName());
        rental.setPicture(filePath);
        fileStorageService.saveFile(picture, filePath);
        rentalService.saveRental(rental);
        return buildMessageResponse(HttpStatus.ACCEPTED,"Rental created !");
    }

    @PutMapping(value = "/api/rentals/{id}", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> updateRental(Authentication auth,
                             @PathVariable("id") final Integer id,
                             @RequestParam("name") String name,
                             @RequestParam("surface") String surface,
                             @RequestParam("price") String price,
                             @RequestParam("description") String description) {
        try {
            Rental rental = new Rental();
            rental.setId(id);
            rental.setName(name);
            rental.setSurface(BigDecimal.valueOf(Double.parseDouble(surface)));
            rental.setPrice(BigDecimal.valueOf(Double.parseDouble(price)));
            rental.setDescription(description);
            rentalService.updateRental(rental);
            return buildMessageResponse(HttpStatus.OK, "Rental Updated !");
        } catch (RentalException e) {
            return buildMessageResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping(value = "/api/rentals/{id}", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> getRental(@PathVariable("id") final Integer id) {
        try {
            Rental rental = rentalService.getRental(id);
            return ResponseEntity.status(HttpStatus.ACCEPTED)
                    .contentType(APPLICATION_JSON)
                    .body(new Gson().toJson(rental));
        } catch (RentalException e) {
            return buildMessageResponse(HttpStatus.BAD_REQUEST, e.getMessage());
        }
    }

    @GetMapping(value = "/api/rentals", produces = APPLICATION_JSON_VALUE)
    public ResponseEntity<String> listRentals() {
        return buildNamedObjectResponse(HttpStatus.OK, "rentals", rentalService.listRentals());
    }

}
