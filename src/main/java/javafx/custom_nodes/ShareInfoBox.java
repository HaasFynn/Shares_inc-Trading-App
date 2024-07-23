package javafx.custom_nodes;

import console.entities.Share;
import console.entities.Tag;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

import java.util.Random;
import java.util.Set;

public class ShareInfoBox {

    private final Random rand;
    private final Share share;
    private final boolean hasGained;

    public ShareInfoBox(Share share) {
        this.rand = new Random();
        this.share = share;
        this.hasGained = rand.nextBoolean();
    }

    public String getName() {
        return share.getName();
    }

    public Text getRevenue() {
        Text revenue = new Text(share.getRevenue() + "%");
        revenue.setFill(hasGained ? Color.GREEN : Color.RED);
        return revenue;
    }

    public Set<Tag> getTags() {
        return share.getTags();
    }

}