package javafx.login;

import javafx.assets.LanguagePack;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

public class LoginPane extends GridPane {
    private final LoginController controller;
    private final Font font;

    public LoginPane(Font font) {
        this.controller = new LoginController(this);
        this.font = font;
        build();
    }

    ImageView logo;
    ChoiceBox<String> languageBox;
    VBox loginBox;
    Text title;
    VBox usernameBox;
    Label usernameLabel;
    TextField usernameField;
    VBox passwordBox;
    Label passwordLabel;
    PasswordField passwordField;
    HBox buttonBox;
    Button submitButton;
    Button resetButton;


    private void build() {
        setMinSize(200, 150);
        setVgap(10);
        addListeners();
        add(getLogo(), 0, 0);
        add(getLanguageBox(), 2, 0);
        add(getLoginBox(), 1, 1);
    }

    private ImageView getLogo() {
        logo = new ImageView(new Image("\\image\\shares_inc._logo.png"));
        logo.setFitHeight(46.6452205882);
        logo.setFitWidth(175);
        return logo;
    }

    public ChoiceBox<String> getLanguageBox() {
        ObservableList<String> options = FXCollections.observableArrayList(LanguagePack.getSupportedLocaleStrings());
        if (options.isEmpty()) {
            options = FXCollections.observableArrayList();
        }
        languageBox = new ChoiceBox<>(options);
        languageBox.getSelectionModel().selectFirst();
        languageBox.getSelectionModel().selectedItemProperty().addListener((observable, oldLanguage, newLanguage) -> {
            controller.handleLanguageChange(newLanguage);
        });
        return languageBox;
    }

    private VBox getLoginBox() {
        loginBox = new VBox();
        loginBox.getChildren().addAll(getTitle(), getUsernameBox(), getPasswordBox(), getButtonBox());
        return loginBox;
    }

    private Text getTitle() {
        title = new Text();
        title.textProperty().bind(getValue("login.title"));
        title.getStyleClass().addAll("h1", "strong");
        title.setFont(font);
        return title;
    }

    private VBox getUsernameBox() {
        usernameBox = new VBox();
        usernameBox.setSpacing(5);
        usernameBox.getChildren().addAll(getUsernameLabel(), getUsernameField());
        return usernameBox;
    }

    private Label getUsernameLabel() {
        usernameLabel = new Label();
        usernameLabel.textProperty().bind(getValue("login.username.label"));
        title.getStyleClass().addAll("lbl-default");
        return usernameLabel;
    }

    private TextField getUsernameField() {
        usernameField = new TextField();
        usernameField.promptTextProperty().bind(getValue("login.username.field"));
        usernameField.getStyleClass().add("p");
        usernameField.setFont(font);
        return usernameField;
    }

    private VBox getPasswordBox() {
        passwordBox = new VBox();
        passwordBox.setSpacing(5);
        passwordBox.getChildren().addAll(getPasswordLabel(), getPasswordField());
        return passwordBox;
    }

    private Label getPasswordLabel() {
        passwordLabel = new Label();
        passwordLabel.textProperty().bind(getValue("login.password.label"));
        title.getStyleClass().addAll("lbl-default");
        return passwordLabel;
    }

    private PasswordField getPasswordField() {
        passwordField = new PasswordField();
        passwordField.promptTextProperty().bind(getValue("login.password.field"));
        passwordField.getStyleClass().add("p");
        passwordField.setFont(font);
        return passwordField;
    }

    private HBox getButtonBox() {
        buttonBox = new HBox();
        buttonBox.setSpacing(15);
        buttonBox.getChildren().addAll(getSubmitButton(), getResetButton());
        return buttonBox;
    }

    private Button getSubmitButton() {
        submitButton = new Button();
        submitButton.setOnMouseClicked(event -> controller.handleLoginAction());
        submitButton.textProperty().bind(getValue("login.button.submit"));
        submitButton.getStyleClass().addAll("btn-sm", "btn-success");
        submitButton.setFont(font);
        return submitButton;
    }

    private Button getResetButton() {
        resetButton = new Button();
        resetButton.setOnMouseClicked(event -> controller.handlePasswordResetButtonAction());
        resetButton.textProperty().bind(getValue("share.button.reset"));
        resetButton.getStyleClass().addAll("btn-sm", "btn-default");
        resetButton.setFont(font);
        return resetButton;
    }

    private void addListeners() {
        setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                controller.handleLoginAction();
            }
        });
    }

    private StringBinding getValue(String key) {
        return LanguagePack.createStringBinding(key);
    }


}
