package console.entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

/**
 * The type Portfolio.
 */
@Entity
@Getter
@Table(name = "share_portfolio")
public class Portfolio {

    @Column(name = "id")
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "share_id")
    private long shareId;

    @Column(name = "user_id")
    private long userId;


    @Column(name = "amount")
    @Setter
    private int amount;

    /**
     * Instantiates a new Portfolio.
     */
    public Portfolio() {
    }

    /**
     * Instantiates a new Portfolio.
     *
     * @param userId  the user id
     * @param shareId the share id
     * @param amount  the amount
     */
    public Portfolio(long userId, long shareId, int amount) {
        this.userId = userId;
        this.shareId = shareId;
        this.amount = amount;
    }

    @Override
    public String toString() {
        return "";
    }
}
