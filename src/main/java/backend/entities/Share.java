package backend.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * The type Share.
 */
@Entity
@Getter
@Table(name = "shares")
public class Share {
    /**
     * The ID.
     */
    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    public long Id;

    /**
     * The Name.
     */
    @Column(name = "name")
    @Setter
    private String name;

    /**
     * The Shortl.
     */
    @Column(name = "shortl")
    @Setter
    private String shortl;

    /**
     * The Price per share.
     */
    @Column(name = "pricepershare")
    @Setter
    private double pricePerShare;

    /**
     * The Stock return.
     */
    @Column(name = "stockreturn")
    @Setter
    private double stockReturn;

    /**
     * The Existing shares amount.
     */
    @Column(name = "existingsharesamount")
    @Setter
    private int existingSharesAmount;

    /**
     * The Date.
     */
    @Column(name = "dateOfEntry")
    @Setter
    private LocalDateTime date;

}


