package entities;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Entity
@Table(name = "share_portfolio")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "share_id")
    private long shareId;

    @Column(name = "user_id")
    private long userId;

    @Setter
    @Column(name = "amount")
    private int amount;

    public Portfolio() {
    }

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
