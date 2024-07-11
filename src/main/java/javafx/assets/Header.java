package javafx.assets;

import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;

public class Header extends VBox {
//TODO: Create

    public Header() {
        build();
    }

    private void build() {

    }

    private Label buildTitle() {
        Label label = new Label();
        label.getStyleClass().add("h1");
        return label;
    }

    private Line buildHeaderLine() {
        Line line = new Line();
        line.setStartX(0);
        line.setStartY(0);
        line.endXProperty().bind(getStage().widthProperty());
        return line;
    }

    private VBox buildHeader(Label title, Line headerLine) {
        VBox box = new VBox(title, headerLine);
        box.getStyleClass().addAll("header");
        return box;
    }
}
