package javafx.share_creation;

import javafx.assets.LanguagePack;
import javafx.beans.binding.StringBinding;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class ShareCreatorPane extends GridPane {
    final Stage stage;
    private final ShareCreatorController controller;
    private final Font font;
    public final double stageHeight = 500;
    public final double stageWidth = 400;

    public ShareCreatorPane(Stage stage, Font font) {
        this.stage = stage;
        this.controller = new ShareCreatorController(this);
        this.font = font;
        build();
    }

    VBox mainBox;
    Text title;
    VBox inputBox;
    Label inputLabel;
    TextField inputField;
    Button submitButton;
    Text statusText;

    void build() {
        setMinSize(200, 150);
        setVgap(10);
        addListeners();
        add(getMainBox(), 0, 0);
        stage.setHeight(stageHeight);
        stage.setWidth(stageWidth);
        stage.hide();
        stage.show();
    }

    private void addListeners() {
        setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                controller.handleOnEnter();
            }
            event.consume();
        });
    }

    private VBox getMainBox() {
        mainBox = new VBox();
        mainBox.setSpacing(10);
        mainBox.setPadding(new Insets(20, 20, 20, 20));
        mainBox.getChildren().addAll(getTitle(), getInputBox());
        return mainBox;
    }

    private Text getTitle() {
        title = new Text();
        title.textProperty().bind(getValueByKey("share.creator.title"));
        title.getStyleClass().addAll("h1", "strong");
        title.setFont(font);
        return title;
    }

    private VBox getInputBox() {
        inputBox = new VBox();
        inputBox.setSpacing(5);
        inputBox.getChildren().addAll(getInputLabel(), getInputField(), getSubmitButton(), getStatusText());
        return inputBox;
    }

    private Label getInputLabel() {
        inputLabel = new Label();
        inputLabel.textProperty().bind(getValueByKey("share.creator.input.label"));
        inputLabel.getStyleClass().add("p");
        inputLabel.setFont(font);
        return inputLabel;
    }

    private TextField getInputField() {
        inputField = new TextField();
        addInputFieldListeners();
        inputField.promptTextProperty().bind(getValueByKey("share.creator.input.label"));
        inputField.getStyleClass().add("p");
        inputField.setFont(font);
        return inputField;
    }

    private void addInputFieldListeners() {
        inputField.textProperty().addListener((observable, oldValue, newValue) -> {
            controller.handleInputValidation(oldValue, newValue);
        });
    }

    private Button getSubmitButton() {
        submitButton = new Button();
        submitButton.textProperty().bind(getValueByKey("share.creator.submit.button"));
        submitButton.getStyleClass().addAll("btn-sm", "btn-success");
        submitButton.setFont(font);
        submitButton.setOnMouseClicked(event -> controller.handleOnEnter());
        return submitButton;
    }

    private Text getStatusText() {
        statusText = new Text();
        statusText.getStyleClass().addAll("p");
        statusText.setFont(font);
        statusText.setVisible(false);
        return statusText;
    }

    StringBinding getValueByKey(String key) {
        return LanguagePack.createStringBinding(key);
    }
}