package javafx.assets;

import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Getter;

import java.util.Random;

@Getter
public class ShareInfoBox extends HBox {

    private Random rand;
    private final Text SPACE = new Text(" - ");
    private String shareName;
    private double revenue;
    private boolean hasGained;

    public ShareInfoBox(String shareName) {
        this.rand = new Random();
        this.shareName = shareName;
        this.revenue = getRandomRevenue();
        this.hasGained = rand.nextBoolean();
    }

    public ShareInfoBox(String shareName, double revenue, boolean hasGained) {
        this.shareName = shareName;
        this.revenue = revenue;
        this.hasGained = hasGained;
    }

    private double getRandomRevenue() {
        return Math.round(rand.nextDouble(1, 8) * 100) / 100.0;
    }
    private Color getColor(boolean hasGained) {
        return hasGained ? Color.rgb(4, 180, 4, 0.94) : Color.RED;
    }
}