package console.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Set;

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
    private long Id;

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

    /**w
     * The Stock return.
     */
    @Column(name = "stockreturn")
    @Setter
    private double revenue;

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

    @ManyToMany
    @JoinTable(name = "share_tag")
    private Set<Tag> tags;
}


