package fr.fredgodard.chatop.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Data
@Entity
@Table(
        name = "USERS",
        uniqueConstraints = {
                @UniqueConstraint(name = "email_unicity", columnNames = {"email"})
        }
)

public class ChatopUserEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "email")
    private String email;

    @Column(name = "name")
    private String name;

    @Column(name = "password")
    private String password;

    @OneToMany
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    private List<ChatopRentalEntity> chatopRentalEntities;

    @OneToMany
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private List<ChatopMessageEntity> chatopMessageEntities;

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

        ChatopUserEntity that = (ChatopUserEntity) o;
        if (id != that.id) return false;
        if (!Objects.equals(email, that.email)) return false;
        if (!Objects.equals(name, that.name)) return false;
        if (!Objects.equals(password, that.password)) return false;
        if (!Objects.equals(creationDate, that.creationDate)) return false;
        if (!Objects.equals(updatedDate, that.updatedDate)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        int result = id;
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (name != null ? name.hashCode() : 0);
        result = 31 * result + (password != null ? password.hashCode() : 0);
        result = 31 * result + (creationDate != null ? creationDate.hashCode() : 0);
        result = 31 * result + (updatedDate != null ? updatedDate.hashCode() : 0);
        return result;
    }

}
