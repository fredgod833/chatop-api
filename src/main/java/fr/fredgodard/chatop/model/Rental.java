package fr.fredgodard.chatop.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class Rental implements Serializable {

    private Integer id;

    private Integer owner_id;

    private String name;

    private BigDecimal surface;

    private BigDecimal price;

    private String description;

    private String picture;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;

}
