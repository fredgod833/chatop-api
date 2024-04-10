package fr.fredgodard.chatop.controller;

import fr.fredgodard.chatop.exceptions.AuthException;
import fr.fredgodard.chatop.exceptions.RentalException;
import fr.fredgodard.chatop.model.ApiResponse;
import fr.fredgodard.chatop.model.Client;
import fr.fredgodard.chatop.model.Rental;
import fr.fredgodard.chatop.model.RentalList;
import fr.fredgodard.chatop.service.ChatopUserService;
import fr.fredgodard.chatop.service.FileStorageService;
import fr.fredgodard.chatop.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.*;

@RestController
@Tag(name="Rentals", description="Create, view and update rentals announcements.")
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
    @Operation(summary="Create rental announcement.", description = "Post announcement form as multipart-form-data with picture.")
    public ResponseEntity<ApiResponse> createRental(
                             Authentication auth,
                             @RequestParam("picture") MultipartFile picture,
                             @RequestParam("name") String name,
                             @RequestParam("surface") String surface,
                             @RequestParam("price") String price,
                             @RequestParam("description") String description) throws AuthException {

        Rental rental = new Rental();
        Client client = chatopUserService.loadConnectedUser(auth);
        rental.setOwner_id(client.getId());

        rental.setName(name);
        rental.setSurface(BigDecimal.valueOf(Double.parseDouble(surface)));
        rental.setPrice(BigDecimal.valueOf(Double.parseDouble(price)));
        rental.setDescription(description);

        // définition du chemin (relatif) de stockage de l'image.
        final String filePath = String.format("%1$s/%2$s",rental.getOwner_id(), picture.getName());
        rental.setPicture(filePath);

        fileStorageService.saveFile(picture, filePath);
        rentalService.saveRental(rental);

        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(new ApiResponse("Rental created !"));
    }

    @PutMapping(value = "/api/rentals/{id}", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary="Update an existing rental.", description = "Update announcement form as multipart-form-data without picture.")
    public ResponseEntity<ApiResponse> updateRental(Authentication auth,
                             @PathVariable("id") final Integer id,
                             @RequestParam("name") String name,
                             @RequestParam("surface") String surface,
                             @RequestParam("price") String price,
                             @RequestParam("description") String description) throws RentalException {

            Rental rental = new Rental();
            rental.setId(id);
            rental.setName(name);
            rental.setSurface(BigDecimal.valueOf(Double.parseDouble(surface)));
            rental.setPrice(BigDecimal.valueOf(Double.parseDouble(price)));
            rental.setDescription(description);
            rentalService.updateRental(rental);

            return ResponseEntity
                    .status(OK)
                    .contentType(APPLICATION_JSON)
                    .body(new ApiResponse("Rental Updated !"));
    }

    @GetMapping(value = "/api/rentals/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary="Get an existing rental by id.")
    public ResponseEntity<Rental> getRental(@PathVariable("id") final Integer id) throws RentalException {
        Rental rental = rentalService.getRental(id);
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(rental);
    }

    @GetMapping(value = "/api/rentals", produces = APPLICATION_JSON_VALUE)
    @Operation(summary="List all rentals.")
    public ResponseEntity<RentalList> listRentals() {
        RentalList result = new RentalList();
        result.addRentals(rentalService.listRentals());
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(result);
    }

}
