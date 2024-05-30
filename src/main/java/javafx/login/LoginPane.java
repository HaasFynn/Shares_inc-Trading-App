package javafx.login;

import javafx.PaneParent;
import javafx.assets.LanguagePack;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class LoginPane extends PaneParent {
    private final LoginController controller;
    private static final double TEXTFIELD_MIN_WIDTH = 320;
    private static final double TEXTFIELD_MIN_HEIGHT = 23;
    private static final double BUTTON_WIDTH = 80;
    private static final double BUTTON_HEIGHT = 23;
    public static final double STAGE_WIDTH = 420;
    public static final double STAGE_HEIGHT = 500;

    public LoginPane(Stage stage, Font font) {
        super(stage, font);
        this.controller = new LoginController(this);
        build();
    }

    ChoiceBox<String> languageChanger;
    //Login
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
    Text statusText;
    //Reset
    Text resetText;
    VBox changePasswordBox;
    Label newPasswordLabel;
    PasswordField newPasswordField;
    Label repeatPasswordLabel;
    PasswordField repeatPasswordField;
    Button resetButton;


    protected void build() {
        setMinSize(200, 150);
        setVgap(10);
        createNodes();
        addListeners();
        add(languageChanger, 1, 1);
        add(loginBox, 1, 2);
        setAlignment(Pos.CENTER);
        adjustWindow();
    }

    private void createNodes() {
        setLanguageChanger();
        setTitle();
        setUsernameLabel();
        setUsernameField();
        setPasswordLabel();
        setPasswordField();
        setSubmitButton();
        setStatusText();

        setResetText();
        setNewPasswordLabel();
        setNewPasswordField();
        setRepeatPasswordLabel();
        setRepeatPasswordField();
        setResetButton();

        setUsernameBox();
        setPasswordBox();
        setButtonBox();
        setChangePasswordBox();
        setLoginBox();
    }

    private void adjustWindow() {
        stage.setWidth(STAGE_WIDTH);
        stage.setHeight(STAGE_HEIGHT);
        stage.centerOnScreen();
        stage.setResizable(false);
    }

    private void setLanguageChanger() {
        ObservableList<String> options = FXCollections.observableArrayList(LanguagePack.getSupportedLocaleStrings());
        if (options.isEmpty()) {
            options = FXCollections.observableArrayList();
        }
        languageChanger = new ChoiceBox<>(options);
        languageChanger.getSelectionModel().selectFirst();
        languageChanger.getStyleClass().add("btn-xs");
        languageChanger.getSelectionModel().selectedItemProperty().addListener((observable, oldLanguage, newLanguage) -> controller.handleLanguageChange(newLanguage));
    }

    private void setLoginBox() {
        loginBox = new VBox();
        loginBox.setSpacing(15);
        loginBox.getChildren().addAll(title, usernameBox, passwordBox, buttonBox, changePasswordBox, statusText);
        loginBox.setAlignment(Pos.CENTER);
    }

    private void setTitle() {
        title = new Text();
        title.textProperty().bind(getValue("login.text.title"));
        title.getStyleClass().addAll("h1", "strong");
        title.setFont(font);
    }

    private void setUsernameBox() {
        usernameBox = new VBox();
        usernameBox.setSpacing(5);
        usernameBox.getChildren().addAll(usernameLabel, usernameField);
    }

    private void setUsernameLabel() {
        usernameLabel = new Label();
        usernameLabel.textProperty().bind(getValue("login.username.label"));
        title.getStyleClass().addAll("lbl-default");
    }

    private void setUsernameField() {
        usernameField = new TextField();
        usernameField.promptTextProperty().bind(getValue("login.username.field"));
        usernameField.getStyleClass().add("p");
        usernameField.setFont(font);
        //That the Fields keep their size when changing lang
        usernameField.setMinHeight(TEXTFIELD_MIN_HEIGHT);
        usernameField.setMinWidth(TEXTFIELD_MIN_WIDTH);
    }

    private void setPasswordBox() {
        passwordBox = new VBox();
        passwordBox.setSpacing(2);
        passwordBox.getChildren().addAll(passwordLabel, passwordField);
    }

    private void setPasswordLabel() {
        passwordLabel = new Label();
        passwordLabel.textProperty().bind(getValue("login.password.label"));
        title.getStyleClass().addAll("lbl-default");
    }


    private void setPasswordField() {
        passwordField = new PasswordField();
        passwordField.promptTextProperty().bind(getValue("login.password.field"));
        passwordField.getStyleClass().add("p");
        passwordField.setFont(font);
    }

    private void setButtonBox() {
        buttonBox = new HBox();
        buttonBox.setSpacing(15);
        buttonBox.getChildren().addAll(submitButton, resetText);
    }

    private void setSubmitButton() {
        submitButton = new Button();
        submitButton.setOnMouseClicked(event -> controller.handleLoginAction());
        submitButton.textProperty().bind(getValue("login.button.submit"));
        submitButton.getStyleClass().addAll("btn-sm", "btn-success", "strong");
        submitButton.setFont(font);
        submitButton.setMinWidth(BUTTON_WIDTH);
        submitButton.setMinHeight(BUTTON_HEIGHT);
    }

    private void setResetText() {
        resetText = new Text();
        resetText.textProperty().bind(getValue("login.text.reset"));
        resetText.getStyleClass().addAll("p");
        resetText.setUnderline(true);
        resetText.setOnMouseClicked(event -> controller.handleOpenResetBox());
        resetText.setFont(font);
    }

    private void setStatusText() {
        statusText = new Text();
        statusText.setFont(font);
        statusText.setVisible(false);
    }


    private void setChangePasswordBox() {
        changePasswordBox = new VBox();
        changePasswordBox.setSpacing(10);
        changePasswordBox.setVisible(false);
        changePasswordBox.getChildren().addAll(passwordLabel, passwordField, repeatPasswordLabel, repeatPasswordField, resetButton);
    }

    private void setNewPasswordLabel() {
        newPasswordLabel = new Label();
        newPasswordLabel.getStyleClass().add("p");
        newPasswordLabel.textProperty().bind(getValue("login.reset.new.label"));
        newPasswordLabel.setFont(font);
    }

    private void setNewPasswordField() {
        newPasswordField = new PasswordField();
        newPasswordField.promptTextProperty().bind(getValue("login.reset.new.field"));
        newPasswordField.getStyleClass().add("p");
        newPasswordField.setFont(font);
    }

    private void setRepeatPasswordLabel() {
        repeatPasswordLabel = new Label();
        repeatPasswordLabel.getStyleClass().add("p");
        repeatPasswordLabel.textProperty().bind(getValue("login.reset.repeat.label"));
        repeatPasswordLabel.setFont(font);
    }

    private void setRepeatPasswordField() {
        repeatPasswordField = new PasswordField();
        repeatPasswordField.promptTextProperty().bind(getValue("login.reset.repeat.field"));
        repeatPasswordField.getStyleClass().add("p");
        repeatPasswordField.setFont(font);
    }

    private void setResetButton() {
        resetButton = new Button();
        resetButton.setOnMouseClicked(event -> controller.handlePasswordResetButtonAction());
        resetButton.textProperty().bind(getValue("login.reset.submitButton"));
        resetButton.getStyleClass().addAll("btn-sm");
        resetButton.setFont(font);
        resetButton.setMinWidth(BUTTON_WIDTH);
        resetButton.setMinHeight(BUTTON_HEIGHT);
        resetButton.setMaxWidth(BUTTON_WIDTH);
        resetButton.setMaxHeight(BUTTON_HEIGHT);
    }

    private void addListeners() {
        controller.handleOnEnter();
    }

    StringBinding getValue(String key) {
        return LanguagePack.createStringBinding(key);
    }
}
