package javafx.assets;

import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Getter;

import java.util.Random;

@Getter
public class ShareInfoBox {

    private Random rand;
    private final Text SPACE = new Text(" - ");
    private String name;
    private Text revenue;
    private boolean hasGained;

    public ShareInfoBox(String name) {
        this.rand = new Random();
        this.name = name;
        this.revenue = new Text(getRandomRevenue() + "%");
        this.hasGained = rand.nextBoolean();
        setColor();
    }

    public ShareInfoBox(String name, double revenue, boolean hasGained) {
        this.name = name;
        this.revenue = new Text(revenue + "%");
        this.hasGained = hasGained;
    }

    private double getRandomRevenue() {
        return Math.round(rand.nextDouble(1, 8) * 100) / 100.0;
    }

    private void setColor() {
        revenue.setFill(this.hasGained ? Color.rgb(4, 180, 4, 0.94) : Color.RED);
    }
}