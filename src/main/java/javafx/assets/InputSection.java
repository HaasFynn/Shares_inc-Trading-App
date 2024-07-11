package javafx.assets;

import javafx.beans.binding.StringBinding;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import lombok.Getter;

@Getter
public class InputSection extends VBox {
    private static final double SPACING = 10;
    private static final double STAGE_WIDTH = 815;
    private static final double STAGE_HEIGHT = 500;

    public InputSection() {
        build();
    }
    private Label label;
    private TextField input;

    public InputSection(String labelBinding, String fieldValue) {
        build();
        bind(label.textProperty(), labelBinding);
        input.setText(fieldValue);
    }


    private void build() {
        setMinSize(STAGE_WIDTH, STAGE_HEIGHT);
        setSpacing(SPACING);
        createNodes();
        setListener();
        getChildren().addAll(label, input);
    }

    private void createNodes() {
        this.label = buildLabel();
        this.input = buildInputField();
    }

    private Label buildLabel() {
        Label label = new Label();
        label.getStyleClass().addAll("input-label", "label");
        return label;
    }

    private TextField buildInputField() {
        TextField input = new TextField();
        input.getStyleClass().addAll("input-field");
        setListener();
        return input;
    }

    private void setListener() {

    }

    protected void bind(StringProperty text, String key) {
        if (key.isEmpty()) {
            return;
        }
        text.bind(getValueByKey(key));
    }

    protected StringBinding getValueByKey(String key) {
        return LanguagePack.createStringBinding(key);
    }

}
