package javafx.pages;

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
import javafx.scene.text.Text;
import javafx.controllers.ShareCreatorController;
import javafx.stage.Stage;
import lombok.Getter;

@Getter
public class ShareCreatorPane extends GridPane {
    final Stage stage;
    private final ShareCreatorController controller;
    public static final double STAGE_WIDTH = 815;
    public static final double STAGE_HEIGHT = 500;
    private static final double TEXTFIELD_MIN_WIDTH = 320;
    private static final double TEXTFIELD_MIN_HEIGHT = 23;
    private static final double BUTTON_WIDTH = 80;
    private static final double BUTTON_HEIGHT = 23;
    private static final double STANDARD_PADDING = 20;

    public ShareCreatorPane(Stage stage) {
        this.stage = stage;
        this.controller = new ShareCreatorController(this);
        build();
    }

    private VBox body;
    private Text title;
    private VBox inputBox;
    private Label inputLabel;
    private TextField inputField;
    private Button submitButton;
    private Text statusText;

    void build() {
        setMinSize(STAGE_WIDTH, STAGE_HEIGHT);
        setVgap(10);
        createNodes();
        addListeners();
        add(body, 0, 0);
        if (stage.isShowing()) {
            adjustWindow();
        }
    }

    private void createNodes() {
        title = buildTitle();
        inputLabel = buildLabel("share.creator.input.label", "p");
        inputField = buildInputField();
        submitButton = buildSubmitButton();
        statusText = buildStatusText();

        inputBox = buildInputBox(inputLabel, inputField, submitButton, statusText);
        body = buildBody(title, inputBox);
    }

    private void adjustWindow() {
        stage.getScene().getWindow().setHeight(STAGE_HEIGHT);
        stage.getScene().getWindow().setWidth(STAGE_WIDTH);
        stage.centerOnScreen();
        stage.setResizable(true); //Change Goal: Adjustable without problems
    }

    private VBox buildBody(Text text, VBox box1) {
        VBox box = new VBox();
        box.setSpacing(10);
        box.setPadding(new Insets(20, 20, 20, 20));
        box.getChildren().addAll(text, box1);
        return box;
    }

    private Text buildTitle() {
        Text text = new Text();
        text.textProperty().bind(getValueByKey("share.creator.title"));
        text.getStyleClass().addAll("h1", "strong");
        return text;
    }

    private VBox buildInputBox(Label label, TextField field, Button button, Text text) {
        VBox box = new VBox();
        box.setSpacing(5);
        box.getChildren().addAll(label, field, button, text);
        return box;
    }

    private Label buildLabel(String key, String... styleClasses) {
        Label label = new Label();
        label.textProperty().bind(getValueByKey(key));
        label.getStyleClass().addAll(styleClasses);
        return label;
    }

    private TextField buildField(String key, String... styleClasses) {
        TextField field = new TextField();
        field.promptTextProperty().bind(getValueByKey(key));
        field.getStyleClass().addAll(styleClasses);
        return field;
    }

    private Button buildSubmitButton() {
        Button button = buildButton("share.creator.submit.button", "btn-sm", "btn-success");
        button.setOnMouseClicked(event -> controller.handleOnEnter());
        return button;
    }

    private TextField buildInputField() {
        TextField field = buildField("share.creator.input.label", "p");
        field.textProperty().addListener((observable, oldValue, newValue) -> controller.handleInputValidation(oldValue, newValue));
        return field;
    }

    private Button buildButton(String key, String... styleClasses) {
        Button button = new Button();
        button.textProperty().bind(getValueByKey(key));
        button.getStyleClass().addAll(styleClasses);
        return button;
    }

    private Text buildStatusText() {
        Text text = new Text();
        text.getStyleClass().addAll("p");
        text.setVisible(false);
        return text;
    }

    private void addListeners() {
        setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                controller.handleOnEnter();
            }
            event.consume();
        });
    }

    StringBinding getValueByKey(String key) {
        return LanguagePack.createStringBinding(key);
    }
}