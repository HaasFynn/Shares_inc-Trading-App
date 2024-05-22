package javafx.login;

import javafx.assets.LanguagePack;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

public class LoginPane extends GridPane {
    final Stage stage;
    private final LoginController controller;
    final Font font;
    public final double stageHeight = 500;
    public final double stageWidth = 400;

    public LoginPane(Stage stage, Font font) {
        this.stage = stage;
        this.controller = new LoginController(this);
        this.font = font;
        build();
    }

    ChoiceBox<String> languageBox;
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


    private void build() {
        setMinSize(200, 150);
        setVgap(10);
        addListeners();
        add(getLanguageBox(), 2, 0);
        add(getLoginBox(), 1, 1);
        setAlignment(Pos.CENTER);
        stage.setHeight(stageHeight);
        stage.setWidth(stageWidth);
        stage.hide();
        stage.show();
    }

    public ChoiceBox<String> getLanguageBox() {
        ObservableList<String> options = FXCollections.observableArrayList(LanguagePack.getSupportedLocaleStrings());
        if (options.isEmpty()) {
            options = FXCollections.observableArrayList();
        }
        languageBox = new ChoiceBox<>(options);
        languageBox.getSelectionModel().selectFirst();
        languageBox.getStyleClass().add("btn-xs");
        languageBox.getSelectionModel().selectedItemProperty().addListener((observable, oldLanguage, newLanguage) -> {
            controller.handleLanguageChange(newLanguage);
        });
        return languageBox;
    }

    private VBox getLoginBox() {
        loginBox = new VBox();
        loginBox.setSpacing(15);
        loginBox.getChildren().addAll(getTitle(), getUsernameBox(), getPasswordBox(), getButtonBox(), getChangePasswordBox(), getStatusText());
        loginBox.setAlignment(Pos.CENTER);
        return loginBox;
    }

    private Text getTitle() {
        title = new Text();
        title.textProperty().bind(getValue("login.text.title"));
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
        //That the Fields keep their size when changing lang
        usernameField.setMinHeight(23);
        usernameField.setMinWidth(246);
        return usernameField;
    }

    private VBox getPasswordBox() {
        passwordBox = new VBox();
        passwordBox.setSpacing(2);
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
        buttonBox.getChildren().addAll(getSubmitButton(), getResetLink());
        return buttonBox;
    }

    private Button getSubmitButton() {
        submitButton = new Button();
        submitButton.setOnMouseClicked(event -> controller.handleLoginAction());
        submitButton.textProperty().bind(getValue("login.button.submit"));
        submitButton.getStyleClass().addAll("btn-sm", "btn-success", "strong");
        submitButton.setFont(font);
        submitButton.setMinWidth(80);
        submitButton.setMinHeight(23);
        return submitButton;
    }

    private Text getResetLink() {
        resetText = new Text();
        resetText.textProperty().bind(getValue("login.text.reset"));
        resetText.getStyleClass().addAll("p");
        resetText.setUnderline(true);
        resetText.setOnMouseClicked(event -> controller.handleOpenResetBox());
        resetText.setFont(font);
        return resetText;
    }

    private Text getStatusText() {
        statusText = new Text();
        statusText.setFont(font);
        statusText.setVisible(false);
        return statusText;
    }


    private VBox getChangePasswordBox() {
        changePasswordBox = new VBox();
        changePasswordBox.setSpacing(10);
        changePasswordBox.setVisible(false);
        changePasswordBox.getChildren().addAll(getNewPasswordLabel(), getNewPasswordField(), getRepeatPasswordLabel(), getRepeatPasswordField(), getResetButton());
        return changePasswordBox;
    }

    private Label getNewPasswordLabel() {
        newPasswordLabel = new Label();
        newPasswordLabel.getStyleClass().add("p");
        newPasswordLabel.textProperty().bind(getValue("login.reset.new.label"));
        newPasswordLabel.setFont(font);
        return newPasswordLabel;
    }

    private PasswordField getNewPasswordField() {
        newPasswordField = new PasswordField();
        newPasswordField.promptTextProperty().bind(getValue("login.reset.new.field"));
        newPasswordField.getStyleClass().add("p");
        newPasswordField.setFont(font);
        return newPasswordField;
    }

    private Label getRepeatPasswordLabel() {
        repeatPasswordLabel = new Label();
        repeatPasswordLabel.getStyleClass().add("p");
        repeatPasswordLabel.textProperty().bind(getValue("login.reset.repeat.label"));
        repeatPasswordLabel.setFont(font);
        return repeatPasswordLabel;
    }

    private TextField getRepeatPasswordField() {
        repeatPasswordField = new PasswordField();
        repeatPasswordField.promptTextProperty().bind(getValue("login.reset.repeat.field"));
        repeatPasswordField.getStyleClass().add("p");
        repeatPasswordField.setFont(font);
        return repeatPasswordField;
    }

    private Button getResetButton() {
        resetButton = new Button();
        resetButton.setOnMouseClicked(event -> controller.handlePasswordResetButtonAction());
        resetButton.textProperty().bind(getValue("login.reset.submitButton"));
        resetButton.getStyleClass().addAll("btn-sm");
        resetButton.setFont(font);
        resetButton.setMinWidth(80);
        resetButton.setMinHeight(23);
        resetButton.setMaxWidth(80);
        resetButton.setMaxHeight(23);
        return resetButton;
    }

    private void addListeners() {
        setOnKeyPressed(keyEvent -> {
            if (keyEvent.getCode() == KeyCode.ENTER) {
                controller.handleLoginAction();
            }
        });
    }

    StringBinding getValue(String key) {
        return LanguagePack.createStringBinding(key);
    }
}
