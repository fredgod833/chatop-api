package fr.fredgodard.chatop.controller;

import fr.fredgodard.chatop.exceptions.*;
import fr.fredgodard.chatop.model.ApiMessage;
import fr.fredgodard.chatop.model.Client;
import fr.fredgodard.chatop.model.Rental;
import fr.fredgodard.chatop.model.RentalList;
import fr.fredgodard.chatop.service.ChatopUserService;
import fr.fredgodard.chatop.service.FileStorageService;
import fr.fredgodard.chatop.service.RentalService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

import static org.apache.logging.log4j.util.Strings.isBlank;
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
    @SecurityRequirement(name = "Bearer JWT Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "create confirmation message.",
                    content = @Content(schema = @Schema(implementation = ApiMessage.class))
            ),
            @ApiResponse(responseCode = "400",
                    description = "Incorrect values.",
                    content = @Content(schema = @Schema(implementation = ApiMessage.class))
            ),
            @ApiResponse(responseCode = "500",
                    description = "Storage exception.",
                    content = @Content(schema = @Schema(implementation = ApiMessage.class))
            )})
    public ResponseEntity<ApiMessage> createRental(
                             Authentication auth,
                             @RequestParam("picture") MultipartFile picture,
                             @RequestParam("name") String name,
                             @RequestParam("surface") String surface,
                             @RequestParam("price") String price,
                             @RequestParam("description") String description) throws AuthException, RentalException, FileStorageException, RentalNotFoundException {

        Rental rental = new Rental();
        Client client = chatopUserService.loadConnectedUser(auth);
        rental.setOwner_id(client.getId());
        fillRentalDatas(name, surface, price, description, rental);

        // Opération en 3 temps (en attendant les transactions inter-services)  :
        //   création des infos en bdd (pour obtenir l'id de la location)
        //   sauvegarde de l'image sur le serveur de fichier (avec l'id de location)
        //   mise à jour du chemin de l'image en bdd ou suppression de la location si non sauvegarde de l'image.
        try {
            rental = rentalService.saveRental(rental);
            // définition du chemin (relatif) de stockage de l'image.
            final String filePath = String.format("%1$s/%2$s/%3$s", rental.getOwner_id(), rental.getId(), picture.getName());
            fileStorageService.saveFile(picture, filePath);
            rental.setPicture(filePath);
            rentalService.updateRental(rental);

        } catch (FileStorageException e) {
            rentalService.deleteRental(rental.getId());
        }

        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(new ApiMessage("Rental created !"));
    }

    @PutMapping(value = "/api/rentals/{id}", consumes = MULTIPART_FORM_DATA_VALUE, produces = APPLICATION_JSON_VALUE)
    @Operation(summary="Update an existing rental.", description = "Update announcement form as multipart-form-data without picture.")
    @SecurityRequirement(name = "Bearer JWT Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "update confirmation message.",
                    content = @Content(schema = @Schema(implementation = ApiMessage.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Rental not found.",
                    content = @Content(schema = @Schema(implementation = ApiMessage.class))
            ),
            @ApiResponse(responseCode = "400",
                    description = "Incorrect values.",
                    content = @Content(schema = @Schema(implementation = ApiMessage.class))
            )})
    public ResponseEntity<ApiMessage> updateRental( @PathVariable("id") final Integer id,
                                                    @RequestParam("name") String name,
                                                    @RequestParam("surface") String surface,
                                                    @RequestParam("price") String price,
                                                    @RequestParam("description") String description) throws RentalNotFoundException, RentalException {

            Rental rental = new Rental();
            rental.setId(id);
            fillRentalDatas(name, surface, price, description, rental);
            rentalService.updateRental(rental);
            return ResponseEntity
                    .status(OK)
                    .contentType(APPLICATION_JSON)
                    .body(new ApiMessage("Rental Updated !"));
    }

    private static void fillRentalDatas(String name, String surface, String price, String description, Rental rental) throws RentalException {
        rental.setName(name);
        if (surface != null) {
            try {
                BigDecimal val = BigDecimal.valueOf(Double.parseDouble(surface));
                rental.setSurface(val);
            } catch (NumberFormatException e) {
                throw new RentalException("surface should be a numeric value.", e);
            }
        }
        if (price != null) {
            try {
                BigDecimal val = BigDecimal.valueOf(Double.parseDouble(price));
                rental.setPrice(val);
            } catch (NumberFormatException e) {
                throw new RentalException("price should be a numeric value.", e);
            }
        }
        rental.setDescription(description);
    }

    @GetMapping(value = "/api/rentals/{id}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary="Get an existing rental by id.")
    @SecurityRequirement(name = "Bearer JWT Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "return searched rental.",
                    content = @Content(schema = @Schema(implementation = Rental.class))
            ),
            @ApiResponse(responseCode = "404",
                    description = "Rental not found.",
                    content = @Content(schema = @Schema(implementation = ApiMessage.class))
            )})
    public ResponseEntity<Rental> getRental(@PathVariable("id") final Integer id) throws RentalNotFoundException {
        Rental rental = rentalService.getRental(id);
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(rental);
    }

    @GetMapping(value = "/api/rentals", produces = APPLICATION_JSON_VALUE)
    @Operation(summary="List all rentals.")
    @SecurityRequirement(name = "Bearer JWT Authentication")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200",
                    description = "List of rentals.",
                    content = @Content(schema = @Schema(implementation = RentalList.class))
            )})
    public ResponseEntity<RentalList> listRentals() {
        RentalList result = new RentalList();
        result.addRentals(rentalService.listRentals());
        return ResponseEntity
                .status(OK)
                .contentType(APPLICATION_JSON)
                .body(result);
    }

}
