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
    public static final double STAGE_WIDTH = 815;
    public static final double STAGE_HEIGHT = 500;

    public ShareCreatorPane(Stage stage, Font font) {
        this.stage = stage;
        this.controller = new ShareCreatorController(this);
        this.font = font;
        build();
    }

    VBox body;
    Text title;
    VBox inputBox;
    Label inputLabel;
    TextField inputField;
    Button submitButton;
    Text statusText;

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
        setTitle();
        setInputLabel();
        setInputField();
        setSubmitButton();
        setStatusText();

        setInputBox();
        setBody();
    }

    private void adjustWindow() {
        stage.getScene().getWindow().setHeight(STAGE_HEIGHT);
        stage.getScene().getWindow().setWidth(STAGE_WIDTH);
        stage.centerOnScreen();
        stage.setResizable(false); //Change Goal: Adjustable without problems
    }

    private void setBody() {
        body = new VBox();
        body.setSpacing(10);
        body.setPadding(new Insets(20, 20, 20, 20));
        body.getChildren().addAll(title, inputBox);
    }

    private void setTitle() {
        title = new Text();
        title.textProperty().bind(getValueByKey("share.creator.title"));
        title.getStyleClass().addAll("h1", "strong");
        title.setFont(font);
    }

    private void setInputBox() {
        inputBox = new VBox();
        inputBox.setSpacing(5);
        inputBox.getChildren().addAll(inputLabel, inputField, submitButton, statusText);
    }

    private void setInputLabel() {
        inputLabel = new Label();
        inputLabel.textProperty().bind(getValueByKey("share.creator.input.label"));
        inputLabel.getStyleClass().add("p");
        inputLabel.setFont(font);
    }

    private void setInputField() {
        inputField = new TextField();
        addInputFieldListener();
        inputField.promptTextProperty().bind(getValueByKey("share.creator.input.label"));
        inputField.getStyleClass().add("p");
        inputField.setFont(font);
    }

    private void addInputFieldListener() {
        inputField.textProperty().addListener((observable, oldValue, newValue) -> controller.handleInputValidation(oldValue, newValue));
    }

    private void setSubmitButton() {
        submitButton = new Button();
        submitButton.textProperty().bind(getValueByKey("share.creator.submit.button"));
        submitButton.getStyleClass().addAll("btn-sm", "btn-success");
        submitButton.setFont(font);
        submitButton.setOnMouseClicked(event -> controller.handleOnEnter());
    }

    private void setStatusText() {
        statusText = new Text();
        statusText.getStyleClass().addAll("p");
        statusText.setFont(font);
        statusText.setVisible(false);
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