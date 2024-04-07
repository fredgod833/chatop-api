package fr.fredgodard.chatop.model;

import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

@Data
public class Rental implements Serializable {

    private Integer id;

    private Integer owner_id;

    private String name;

    private BigDecimal surface;

    private BigDecimal price;

    private String description;

    private String picture;

    private Date created_at;

    private Date updated_at;

}
