package javafx.share_creation;

import javafx.assets.LanguagePack;
import backend.dao.ShareDaoImpl;
import backend.functional.EntityManagement;
import javafx.beans.binding.StringBinding;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class ShareCreatorPane extends GridPane {
    private final Scene scene;
    private final ShareCreatorController controller;
    private final Font font;

    public ShareCreatorPane(String font, Scene scene) {
        this.scene = scene;
        this.controller = new ShareCreatorController(this, new ShareDaoImpl(EntityManagement.createEntityManagerFactory().createEntityManager()));
        this.font = Font.font(font);
        create();
    }


    VBox mainBox;
    Text title;
    VBox inputBox;
    Label inputLabel;
    TextField inputField;
    Button submitButton;
    Text statusText;

    void create() {
        setMinSize(200, 150);
        setVgap(10);
        addListeners();
        add(getMainBox(), 0, 0);
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
        inputLabel.getStyleClass().add("lbl-default");
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
        statusText.getStyleClass().addAll("btn-sm", "btn-success");
        statusText.setFont(font);
        statusText.setVisible(false);
        return statusText;
    }

    StringBinding getValueByKey(String key) {
        return LanguagePack.createStringBinding(key);
    }
}