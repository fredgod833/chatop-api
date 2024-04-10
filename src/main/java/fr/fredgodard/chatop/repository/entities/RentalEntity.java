package fr.fredgodard.chatop.repository.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Data
@Entity
@Table(name = "RENTALS")
@EntityListeners(AuditingEntityListener.class)
public class RentalEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "surface")
    private BigDecimal surface;

    @Column(name = "price")
    private BigDecimal price;

    @Column(name = "picture")
    private String picture;

    @Column(name = "description")
    private String description;

    @Column(name = "owner_id", nullable = false)
    private Integer ownerId;

    @CreatedDate
    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime created_at;

    @LastModifiedDate
    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private LocalDateTime updated_at;

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RentalEntity that = (RentalEntity) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(ownerId, that.ownerId)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(surface, that.surface)) return false;
        if (!Objects.equals(price, that.price)) return false;
        if (!Objects.equals(picture, that.picture)) return false;
        if (!Objects.equals(description, that.description)) return false;
        if (!Objects.equals(created_at, that.created_at)) return false;
        if (!Objects.equals(updated_at, that.updated_at)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (surface != null ? surface.hashCode() : 0);
        result = 31 * result + (price != null ? price.hashCode() : 0);
        result = 31 * result + (picture != null ? picture.hashCode() : 0);
        result = 31 * result + (description != null ? description.hashCode() : 0);
        result = 31 * result + ownerId;
        result = 31 * result + (created_at != null ? created_at.hashCode() : 0);
        result = 31 * result + (updated_at != null ? updated_at.hashCode() : 0);
        return result;
    }

}
