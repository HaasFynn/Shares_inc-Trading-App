package javafx.custom_nodes;

import javafx.assets.LanguagePack;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import lombok.Getter;

@Getter
public class Header extends VBox {
//TODO: Create

    private final Stage stage;
    private final String titleKey;

    public Header(Stage stage, String titleKey) {
        this.stage = stage;
        this.titleKey = titleKey;
        build();
    }

    private Label title;
    private Line line;

    private void build() {
        this.title = buildTitle();
        this.line = buildLine();
        this.getChildren().addAll(title, line);
    }

    private Label buildTitle() {
        Label label = new Label();
        label.getStyleClass().addAll("h2", "title");
        bind(label.textProperty());
        return label;
    }

    private Line buildLine() {
        Line line = new Line();
        line.setStartX(0);
        line.setStartY(0);
        line.endXProperty().bind(getStage().widthProperty());
        return line;
    }

    private void bind(StringProperty property) {
        property.bind(LanguagePack.createStringBinding(titleKey));
    }

}
