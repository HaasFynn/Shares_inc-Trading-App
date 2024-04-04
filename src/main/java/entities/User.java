package entities;

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
    /**
     * The Id.
     */
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long Id;

    /**
     * The Username.
     */
    @Column(name = "username")
    @Setter
    private String username;

    /**
     * The Firstname.
     */
    @Column(name = "firstname")
    @Setter
    private String firstname;

    /**
     * The Lastname.
     */
    @Column(name = "lastname")
    @Setter
    private String lastname;

    /**
     * The Email.
     */
    @Column(name = "email")
    @Setter
    private String email;

    /**
     * The Password.
     */
    @Column(name = "password")
    @Setter
    private String password;

    /**
     * The Account balance.
     */
    @Column(name = "accountBalance")
    @Setter
    private double accountBalance;

    /**
     * The Date.
     */
    @Column(name = "creationDate")
    @Setter
    private LocalDateTime date;

    /**
     * Instantiates a new User.
     */
    public User() {
        this.accountBalance = 1000.0;
        this.date = LocalDateTime.now();
    }

}
