package fr.fredgodard.chatop.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.Objects;

@Data
@Entity
@Table(name = "MESSAGES")
public class ChatopMessageEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "rental_id")
    private Integer rentalId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "message", nullable = true, length = 2000)
    private String message;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date creationDate;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        ChatopMessageEntity that = (ChatopMessageEntity) o;

        if (id != that.id) return false;
        if (!Objects.equals(rentalId, that.rentalId)) return false;
        if (!Objects.equals(userId, that.userId)) return false;
        if (!Objects.equals(message, that.message)) return false;
        if (!Objects.equals(creationDate, that.creationDate)) return false;
        if (!Objects.equals(updatedDate, that.updatedDate)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (rentalId != null ? rentalId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (updatedDate != null ? updatedDate.hashCode() : 0);
        return result;
    }

}
