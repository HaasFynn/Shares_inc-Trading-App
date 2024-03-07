package entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.UUID;

/**
 * The type Share.
 */
@Entity
@Table(name = "share")
public class Share {
    /**
     * The Id.
     */
    @Column(name = "id")
    @Id
    @GeneratedValue
    public UUID Id;

    /**
     * The Name.
     */
    @Column(name = "name")
    @Setter
    @Getter
    public String name;

    /**
     * The Shortl.
     */
    @Column(name = "shortl")
    @Setter
    @Getter
    public String shortl;

    /**
     * The Price per share.
     */
    @Column(name = "pricepershare")
    @Setter
    @Getter
    public double pricePerShare;

    /**
     * The Stock return.
     */
    @Column(name = "stockreturn")
    @Setter
    @Getter
    public double stockReturn;

    /**
     * The Existing shares amount.
     */
    @Column(name = "existingsharesamount")
    @Setter
    @Getter
    public int existingSharesAmount;

    /**
     * The Date.
     */
    @Column(name = "dateOfEntry")
    @Setter
    @Getter
    public LocalDateTime date;
}


