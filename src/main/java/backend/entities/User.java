package backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * The type User.
 */
@Getter
@Entity
@Table(name = "users")
public class User {
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long Id;

    @Column(name = "username")
    @Setter
    private String username;

    @Column(name = "firstname")
    @Setter
    private String firstname;

    @Column(name = "lastname")
    @Setter
    private String lastname;

    @Column(name = "email")
    @Setter
    private String email;

    @Column(name = "password")
    @Setter
    private String password;

    @Column(name = "accountBalance")
    @Setter
    private double accountBalance;

    @Column(name = "creationDate")
    @Setter
    private LocalDateTime date;

    public User() {
        this.accountBalance = 1000.0;
        this.date = LocalDateTime.now();
    }

}
