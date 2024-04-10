package fr.fredgodard.chatop.repository.entities;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

@Data
@Entity
@Table(name = "MESSAGES")
@EntityListeners(AuditingEntityListener.class)
public class MessageEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "rental_id")
    private Integer rentalId;

    @Column(name = "user_id", nullable = false)
    private Integer userId;

    @Column(name = "message", length = 2000)
    private String message;

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

        MessageEntity that = (MessageEntity) o;

        if (!Objects.equals(id, that.id)) return false;
        if (!Objects.equals(rentalId, that.rentalId)) return false;
        if (!Objects.equals(userId, that.userId)) return false;
        if (!Objects.equals(message, that.message)) return false;
        if (!Objects.equals(created_at, that.created_at)) return false;
        if (!Objects.equals(updated_at, that.updated_at)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (rentalId != null ? rentalId.hashCode() : 0);
        result = 31 * result + (userId != null ? userId.hashCode() : 0);
        result = 31 * result + (message != null ? message.hashCode() : 0);
        result = 31 * result + (created_at != null ? created_at.hashCode() : 0);
        result = 31 * result + (updated_at != null ? updated_at.hashCode() : 0);
        return result;
    }

}
