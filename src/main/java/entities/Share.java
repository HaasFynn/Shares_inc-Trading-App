package entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

/**
 * The type Share.
 */
@Entity
@Getter
@Table(name = "share")
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
    public String name;

    /**
     * The Shortl.
     */
    @Column(name = "shortl")
    @Setter
    public String shortl;

    /**
     * The Price per share.
     */
    @Column(name = "pricepershare")
    @Setter
    public double pricePerShare;

    /**
     * The Stock return.
     */
    @Column(name = "stockreturn")
    @Setter
    public double stockReturn;

    /**
     * The Existing shares amount.
     */
    @Column(name = "existingsharesamount")
    @Setter
    public int existingSharesAmount;

    /**
     * The Date.
     */
    @Column(name = "dateOfEntry")
    @Setter
    public LocalDateTime date;

}


