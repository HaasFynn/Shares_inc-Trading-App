package javafx.assets;

import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import lombok.Getter;

@Getter
public class ShareInfoBox extends HBox {

    private final Text SPACE = new Text(" - ");
    private String shareName;
    private double diffInPercent;
    private boolean hasGained;

    public ShareInfoBox(String shareName, double changeInPercentage, boolean hasGained) {
        this.shareName = shareName;
        diffInPercent = changeInPercentage;
        this.hasGained = hasGained;
        build();
    }

    private void build() {
        Color color = getColor(hasGained);
        Text name = buildName(shareName, color);
        Text changeInPercentage = buildPercentageText(diffInPercent, color);
        getChildren().addAll(name, SPACE, changeInPercentage);
        getStyleClass().add("info-box");
    }

    private Text buildPercentageText(double diffInPercent, Color color) {
        Text text = buildText(color, "stock-market-text");
        text.setText(diffInPercent + "%");
        return text;
    }

    private Text buildName(String shareName, Color color) {
        Text text = buildText(color, "stock-market-text");
        text.setText(shareName);
        return text;
    }

    private Text buildText(Color color, String... styleClasses) {
        Text text = new Text();
        text.setFill(color);
        text.getStyleClass().addAll(styleClasses);
        return text;
    }

    private Color getColor(boolean hasGained) {
        return hasGained ? Color.rgb(4, 180, 4, 0.94) : Color.RED;
    }
}