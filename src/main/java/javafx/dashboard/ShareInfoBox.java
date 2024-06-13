package javafx.dashboard;

import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

public class ShareInfoBox extends HBox {

    private final Text SPACE = new Text(" - ");

    public ShareInfoBox(String shareName, double changeInPercentage, boolean hasGained) {
        build(shareName, changeInPercentage, hasGained);
    }

    private void build(String shareName, double diffInPercent, boolean hasGained) {
        Color color = getColor(hasGained);
        Text title = buildTitle(shareName, color);
        Text changeInPercentage = buildPercentageText(diffInPercent, color);
        getChildren().addAll(title, SPACE , changeInPercentage);
    }

    private Text buildPercentageText(double diffInPercent, Color color) {
        Text text = buildText(color);
        text.setText(diffInPercent + "%");
        return text;
    }

    private Text buildTitle(String shareName, Color color) {
        Text text = buildText(color);
        text.setText(shareName);
        return text;
    }

    private Text buildText(Color color) {
        Text text = new Text();
        text.setFill(color);
        return text;
    }

    private Color getColor(boolean hasGained) {
        return hasGained ? Color.GREEN : Color.RED;
    }
}