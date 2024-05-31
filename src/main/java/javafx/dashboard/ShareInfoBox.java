package javafx.dashboard;

import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ShareInfoBox extends HBox {
    private final Font font;

    public ShareInfoBox(String shareName, Font font, double changeInPercentage, boolean hasGained) {
        this.font = font;
        build(shareName, changeInPercentage, hasGained);
    }

    public Text title;
    public Text changeInPercentage;

    private void build(String shareName, double diffInPercent, boolean hasGained) {
        Color color = getColor(hasGained);
        this.title = buildTitle(shareName, color);
        this.changeInPercentage = buildPercentageText(diffInPercent, color);
        getChildren().addAll(this.title, this.changeInPercentage);
    }

    private Text buildPercentageText(double diffInPercent, Color color) {
        Text text = buildText(color);
        text.setText(Math.round(diffInPercent) + "%");
        return text;
    }

    private Text buildTitle(String shareName, Color color) {
        Text text = buildText(color);
        text.setText(shareName);
        return text;
    }

    private Text buildText(Color color) {
        Text text = new Text();
        text.setFont(font);
        text.setFill(color);
        return text;
    }

    private Color getColor(boolean hasGained) {
        return hasGained ? Color.GREEN : Color.RED;
    }
}