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
import lombok.Getter;

@Getter
public class LoginPane extends PaneParent {
    private final LoginController controller;
    public static final double STAGE_WIDTH = 420;
    public static final double STAGE_HEIGHT = 500;
    private static final double TEXTFIELD_MIN_WIDTH = 320;
    private static final double TEXTFIELD_MIN_HEIGHT = 23;
    private static final double BUTTON_WIDTH = 80;
    private static final double BUTTON_HEIGHT = 23;

    public LoginPane(Stage stage, Font font) {
        super(stage, font);
        this.controller = new LoginController(this);
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
        setMinSize(200, 150);
        setVgap(10);
        buildNodes();
        addListeners();
        add(languageChanger, 1, 1);
        add(loginBox, 1, 2);
        setAlignment(Pos.CENTER);
        adjustWindow();
    }

    private void buildNodes() {
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
        newPasswordLabel = buildLabel("login.reset.new.label", "p");
        newPasswordField = buildPasswordField("login.reset.new.field", "p");
        repeatPasswordLabel = buildLabel("login.reset.repeat.label", "p");
        repeatPasswordField = buildPasswordField("login.reset.repeat.field", "p");
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
        stage.setWidth(STAGE_WIDTH);
        stage.setHeight(STAGE_HEIGHT);
        stage.centerOnScreen();
        stage.setResizable(false);
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
        box.setSpacing(15);
        box.getChildren().addAll(title, usernameBox, passwordBox, buttonBox, changePasswordBox, statusText);
        box.setAlignment(Pos.CENTER);
        return box;
    }

    private Text buildTitle() {
        Text text = new Text();
        text.textProperty().bind(getValue("login.text.title"));
        text.getStyleClass().addAll("h1", "strong");
        text.setFont(font);
        return text;
    }

    private VBox buildUsernameBox(Label label, TextField field) {
        VBox box = new VBox();
        box.setSpacing(5);
        box.getChildren().addAll(label, field);
        return box;
    }

    private Label buildUsernameLabel() {
        return buildLabel("login.username.label", "p");
    }

    private TextField buildUsernameField() {
        TextField field = new TextField();
        field.promptTextProperty().bind(getValue("login.username.field"));
        field.getStyleClass().add("p");
        field.setFont(font);
        //That the Fields keep their size when changing lang
        field.setMinHeight(TEXTFIELD_MIN_HEIGHT);
        field.setMinWidth(TEXTFIELD_MIN_WIDTH);
        return field;
    }

    private VBox buildPasswordBox(Label label, PasswordField field) {
        VBox box = new VBox();
        box.setSpacing(2);
        box.getChildren().addAll(label, field);
        return box;
    }

    private Label buildPasswordLabel() {
        return buildLabel("login.password.label", "p");
    }


    private PasswordField buildPasswordLoginField() {
        return buildPasswordField("login.password.field", "p");
    }

    private HBox buildButtonBox(Button button, Text text) {
        HBox box = new HBox();
        box.setSpacing(15);
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
        text.getStyleClass().addAll("p");
        text.setUnderline(true);
        text.setOnMouseClicked(event -> controller.handleOpenResetBox());
        text.setFont(font);
        return text;
    }

    private Text buildStatusText() {
        Text text = new Text();
        text.setFont(font);
        text.setVisible(false);
        return text;
    }

    private VBox buildChangePasswordBox(Label label1, PasswordField passwordField1, Label label2, PasswordField passwordField2, Button button) {
        VBox box = new VBox();
        box.setSpacing(10);
        box.setVisible(false);
        box.getChildren().addAll(label1, passwordField1, label2, passwordField2, button);
        return box;
    }

    private Label buildLabel(String key, String... styleClasses) {
        Label label = new Label();
        label.getStyleClass().addAll(styleClasses);
        label.textProperty().bind(getValue(key));
        label.setFont(font);
        return label;
    }

    private Button buildResetButton() {
        Button button = buildButton("login.reset.submitButton", "btn-sm");
        button.setOnMouseClicked(event -> controller.handlePasswordResetButtonAction());
        return button;
    }

    private Button buildButton(String key, String... styleClasses) {
        Button button = new Button();
        button.textProperty().bind(getValue(key));
        button.getStyleClass().addAll(styleClasses);
        button.setFont(font);
        button.setMinWidth(BUTTON_WIDTH);
        button.setMinHeight(BUTTON_HEIGHT);
        button.setMaxWidth(BUTTON_WIDTH);
        button.setMaxHeight(BUTTON_HEIGHT);
        return button;
    }

    private PasswordField buildPasswordField(String key, String... styleClasses) {
        PasswordField field = new PasswordField();
        field.promptTextProperty().bind(getValue(key));
        field.getStyleClass().addAll(styleClasses);
        field.setFont(font);
        field.setMinHeight(TEXTFIELD_MIN_HEIGHT);
        field.setMinWidth(TEXTFIELD_MIN_WIDTH);
        return field;
    }

    private void addListeners() {
        controller.handleOnEnter();
    }

    StringBinding getValue(String key) {
        return LanguagePack.createStringBinding(key);
    }
}
