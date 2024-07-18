package javafx.panes;

import javafx.assets.ColorTheme;
import javafx.assets.LanguagePack;
import javafx.beans.binding.StringBinding;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.eventlisteners.EventListeners;
import javafx.geometry.Pos;
import javafx.controllers.LoginController;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;

/**
 * The type Login pane.
 */
@Getter
public class LoginPane extends CustomPane {
    private final LoginController controller;

    /**
     * Instantiates a new Login pane.
     *
     * @param stage          the stage
     * @param eventListeners the event listeners
     */
    public LoginPane(Stage stage, EventListeners eventListeners) {
        super(stage, eventListeners, null, ColorTheme.DARK);
        this.controller = new LoginController(stage, this, eventListeners);
        styleClasses = new String[]{
                "login.css"
        };

        build();
    }

    private ChoiceBox<String> languageChanger;
    //Login
    private VBox loginBox;
    private Text title;
    private VBox usernameBox;
    private Label usernameLabel;
    private TextField usernameField;
    private VBox passwordBox;
    private Label passwordLabel;
    private PasswordField passwordField;
    private HBox buttonBox;
    private Button submitButton;
    private Text statusText;
    //Reset
    private Text resetText;
    private VBox changePasswordBox;
    private Label newPasswordLabel;
    private PasswordField newPasswordField;
    private Label repeatPasswordLabel;
    private PasswordField repeatPasswordField;
    private Button resetButton;

    protected void build() {
        STAGE_WIDTH = 420;
        STAGE_HEIGHT = 500;
        setMinSize(STAGE_WIDTH, STAGE_HEIGHT);
        setVgap(V_GAP);
        addStyleSheets();
        buildNodes();
        addListeners();
        add(languageChanger, 1, 1);
        add(loginBox, 1, 2);
        setAlignment(Pos.CENTER);
        adjustWindow();
    }

    @Override
    protected void buildNodes() {
        buildLoginComponents();
        buildResetComponents();
        buildUtilities();

        buildSubBoxes();
        loginBox = buildLoginBox();
    }

    private void buildLoginComponents() {
        title = buildTitle();
        usernameLabel = buildUsernameLabel();
        usernameField = buildUsernameField();
        passwordLabel = buildPasswordLabel();
        passwordField = buildPasswordLoginField();
        submitButton = buildSubmitButton();
    }

    private void buildResetComponents() {
        resetText = buildResetText();
        newPasswordLabel = buildLabel("login.reset.label.new", "p");
        newPasswordField = buildPasswordField("login.reset.field.new", "p");
        repeatPasswordLabel = buildLabel("login.reset.label.repeat", "p");
        repeatPasswordField = buildPasswordField("login.reset.field.repeat", "p");
        resetButton = buildResetButton();
    }

    private void buildUtilities() {
        languageChanger = buildLanguageChanger();
        statusText = buildStatusText();
    }

    private void buildSubBoxes() {
        usernameBox = buildUsernameBox(usernameLabel, usernameField);
        passwordBox = buildPasswordBox(passwordLabel, passwordField);
        buttonBox = buildButtonBox(submitButton, resetText);
        changePasswordBox = buildChangePasswordBox(newPasswordLabel, newPasswordField, repeatPasswordLabel, repeatPasswordField, resetButton);
    }

    private void adjustWindow() {
        getStage().setWidth(STAGE_WIDTH);
        getStage().setHeight(STAGE_HEIGHT);
        getStage().centerOnScreen();
        getStage().setResizable(false);
    }

    private ChoiceBox<String> buildLanguageChanger() {
        ObservableList<String> options = FXCollections.observableArrayList(LanguagePack.getSupportedLocaleStrings());
        if (options.isEmpty()) {
            options = FXCollections.observableArrayList();
        }
        ChoiceBox<String> box = new ChoiceBox<>(options);
        box.getSelectionModel().selectFirst();
        box.getStyleClass().add("btn-xs");
        box.getSelectionModel().selectedItemProperty().addListener((observable, oldLanguage, newLanguage) -> controller.handleLanguageChange(newLanguage));
        return box;
    }

    private VBox buildLoginBox() {
        VBox box = new VBox();
        box.getStyleClass().add("login-box");
        box.getChildren().addAll(title, usernameBox, passwordBox, buttonBox, changePasswordBox, statusText);
        return box;
    }

    private Text buildTitle() {
        Text text = new Text();
        text.textProperty().bind(getValue("login.text.title"));
        text.getStyleClass().addAll("h1", "strong");
        return text;
    }

    private VBox buildUsernameBox(Label label, TextField field) {
        VBox box = new VBox();
        box.getStyleClass().add("username-box");
        box.getChildren().addAll(label, field);
        return box;
    }

    private Label buildUsernameLabel() {
        return buildLabel("login.label.username", "p");
    }

    private TextField buildUsernameField() {
        TextField field = new TextField();
        field.promptTextProperty().bind(getValue("login.field.username"));
        field.getStyleClass().addAll("p", "text-field");
        return field;
    }

    private VBox buildPasswordBox(Label label, PasswordField field) {
        VBox box = new VBox();
        field.getStyleClass().addAll("p", "password-box");
        box.getChildren().addAll(label, field);
        return box;
    }

    private Label buildPasswordLabel() {
        return buildLabel("login.label.password", "p");
    }


    private PasswordField buildPasswordLoginField() {
        return buildPasswordField("login.field.password", "p");
    }

    private HBox buildButtonBox(Button button, Text text) {
        HBox box = new HBox();
        box.getStyleClass().add("button-box");
        box.getChildren().addAll(button, text);
        return box;
    }

    private Button buildSubmitButton() {
        Button button = buildButton("login.button.submit", "btn-sm", "btn-success", "strong");
        button.setOnMouseClicked(event -> controller.handleLoginAction());
        return button;
    }

    private Text buildResetText() {
        Text text = new Text();
        text.textProperty().bind(getValue("login.text.reset"));
        text.getStyleClass().addAll("p", "reset-text");
        text.setOnMouseClicked(event -> controller.handleOpenResetBox());
        return text;
    }

    private Text buildStatusText() {
        Text text = new Text();
        text.setVisible(false);
        text.getStyleClass().addAll("status-text");
        return text;
    }

    private VBox buildChangePasswordBox(Label label1, PasswordField passwordField1, Label label2, PasswordField passwordField2, Button button) {
        VBox box = new VBox();
        box.setVisible(false);
        box.getStyleClass().add("reset-box");
        box.getChildren().addAll(label1, passwordField1, label2, passwordField2, button);
        return box;
    }

    private Label buildLabel(String key, String... styleClasses) {
        Label label = new Label();
        label.textProperty().bind(getValue(key));
        label.getStyleClass().addAll(styleClasses);
        return label;
    }

    private Button buildResetButton() {
        Button button = buildButton("login.reset.button.submit", "btn-sm");
        button.setOnMouseClicked(event -> controller.handlePasswordResetButtonAction());
        return button;
    }

    private Button buildButton(String key, String... styleClasses) {
        Button button = new Button();
        button.textProperty().bind(getValue(key));
        button.getStyleClass().addAll(styleClasses);
        button.getStyleClass().add("button");
        return button;
    }

    private PasswordField buildPasswordField(String key, String... styleClasses) {
        PasswordField field = new PasswordField();
        field.promptTextProperty().bind(getValue(key));
        field.getStyleClass().addAll(styleClasses);
        return field;
    }

    private void addListeners() {
        controller.handleOnEnter();
    }

    /**
     * Gets price.
     *
     * @param key the key
     * @return the price
     */
    public StringBinding getValue(String key) {
        return LanguagePack.createStringBinding(key);
    }
}
