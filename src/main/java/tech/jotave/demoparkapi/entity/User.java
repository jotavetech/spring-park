package tech.jotave.demoparkapi.entity;

import jakarta.persistence.*;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

// cria getters e setters automaticamente
@Getter @Setter @NoArgsConstructor
@Entity
@Table(name="users")
public class User implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String username;

    @Column( nullable = false, length = 220)
    private String password;

    // transforma o nome da const em uma string no bd
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 25)
    private Role role = Role.ROLE_CLIENT;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String createdBy;

    private String modifiedBy;

    public enum Role {
        ROLE_ADMIN, ROLE_CLIENT
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                '}';
    }
}
