package entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * The type User.
 */
@Getter
@Entity
@Table(name = "users")
public class User {
    /**
     * The Id.
     */
    @Column(name = "id")
    @Id
    @GeneratedValue
    public UUID Id;

    /**
     * The Username.
     */
    @Column(name = "username")
    @Setter
    public String username;

    /**
     * The Firstname.
     */
    @Column(name = "firstname")
    @Setter
    public String firstname;

    /**
     * The Lastname.
     */
    @Column(name = "lastname")
    @Setter
    public String lastname;

    /**
     * The Email.
     */
    @Column(name = "email")
    @Setter
    public String email;

    /**
     * The Password.
     */
    @Column(name = "password")
    @Setter
    public String password;

    /**
     * The Account balance.
     */
    @Column(name = "accountBalance")
    @Setter
    public double accountBalance;

    /**
     * The Date.
     */
    @Column(name = "creationDate")
    @Setter
    public LocalDateTime date;

    /**
     * Instantiates a new User.
     */
    public User() {
        this.accountBalance = 1000.0;
    }

}
