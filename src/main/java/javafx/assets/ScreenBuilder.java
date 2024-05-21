package javafx.assets;

import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Region;
import javafx.scene.text.Text;
import javafx.util.Builder;

public class ScreenBuilder implements Builder<Region> {

    @Override
    public Region build() {
        BorderPane results = new BorderPane();
        results.setTop(createTop());
        return results;
    }

    private Node createTop() {
        return new Text("This is the Screen Title");
    }
}
