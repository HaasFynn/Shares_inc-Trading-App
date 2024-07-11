package javafx.pages;

import console.entities.User;
import javafx.assets.Header;
import javafx.assets.InputSection;
import javafx.beans.binding.StringBinding;
import javafx.controllers.ProfileController;
import javafx.eventlisteners.EventListeners;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.awt.image.BufferedImage;
import java.util.function.Supplier;

public class ProfilePane extends CustomPane {
    private static final String STYLEPATH = "style/";
    private final ProfileController controller;

    public ProfilePane(Stage stage, EventListeners eventListeners, User user) {
        super(stage, eventListeners, user);
        this.controller = new ProfileController(stage, eventListeners, user);
        build();
    }

    private VBox page;
    private Header header;

    private VBox body;

    private HBox headingBox;
    private VBox profileBox;
    private Label profileLabel;
    private VBox profileImageBox;
    private ImageView profileImage;
    private InputSection usernameInput;

    private InputSection firstNameInput;
    private InputSection lastNameInput;
    private InputSection emailInput;

    private VBox passwordBox;
    private Label passwordInputLabel;
    private PasswordField passwordInput;

    private Button saveButton;

    @Override
    protected void build() {
        setMinSize(STAGE_WIDTH, STAGE_HEIGHT);
        setVgap(V_GAP);
        addStyleSheet();
        createNodes();
        addListeners();
        add(page, 0, 0);
    }

    private void addStyleSheet() {
        getStylesheets().add(STYLEPATH + "profile-pane.css");
    }

    private void createNodes() {
        this.header = new Header();
        createBody();
        this.page = buildPage(header, body);
    }

    private void createBody() {
        createHeadingBox();
        this.firstNameInput = buildFirstnameInput();
        this.lastNameInput = buildLastNameInput();
        this.emailInput = buildEmailInput();
        createPasswordBox();
        this.saveButton = buildSaveButton();
        this.body = buildBody(headingBox, firstNameInput, lastNameInput, emailInput, passwordBox, saveButton);
    }

    private Button buildSaveButton() {
        String[] styleClasses = new String[]{"save-button"};
        Button button = buildButton(styleClasses, "portfolio_pane.button.save");
        button.setOnAction(controller::saveInput);
        return button;
    }

    private Button buildButton(String[] styleClasses, String bindingKey) {
        Button button = new Button();
        bind(button.textProperty(), bindingKey);
        button.getStyleClass().addAll(styleClasses);
        return button;
    }

    private VBox buildBody(Node... nodes) {
        VBox body = new VBox(nodes);
        body.getStyleClass().add("body");
        return body;
    }

    private void createPasswordBox() {
        this.passwordInputLabel = buildPasswordLabel();
        this.passwordInput = buildPasswordInput();
        String[] styleClasses = new String[]{"password-box"};
        this.passwordBox = buildVBox(styleClasses, passwordInputLabel, passwordInput);
    }

    private Label buildPasswordLabel() {
        return buildLabel("profile_pane.label.password", "password-label", "label");
    }

    private InputSection buildFirstnameInput() {
        return new InputSection("profile_pane.input.firstname", controller.user()::getFirstname);
    }

    private InputSection buildLastNameInput() {
        return new InputSection("profile_pane.input.lastname", controller.user()::getLastname);
    }

    private InputSection buildEmailInput() {
        return new InputSection("profile_pane.input.email", controller.user()::getEmail);
    }

    private PasswordField buildPasswordInput() {
        PasswordField passwordField = new PasswordField();
        passwordField.getStyleClass().addAll("password-field");
        return passwordField;
    }

    private void createHeadingBox() {
        createProfileBox();
        this.usernameInput = buildUsernameInput();
        this.headingBox = buildHeadingBox(profileBox, usernameInput);
    }

    private InputSection buildUsernameInput() {
        return new InputSection("profile_pane.input.username", controller.user()::getUsername);
    }

    private void createProfileBox() {
        this.profileLabel = buildProfileLabel();
        buildProfileImageBox();
        String[] styleClasses = new String[]{"profile-box"};
        this.profileBox = buildVBox(styleClasses, profileLabel, profileImageBox);
    }

    private void buildProfileImageBox() {
        this.profileImage = buildProfileImage();
        String[] styleClasses = new String[]{"profile-image-box"};
        this.profileImageBox = buildVBox(styleClasses, profileImage);
    }

    private ImageView buildProfileImage() {
        ImageView image = new ImageView(new Image("assets/image/shares_inc._logo.png"));
        image.getStyleClass().add("profile-image");
        return image;
    }

    private Label buildProfileLabel() {
        return buildLabel("profile_pane.label.image", "profile-pic-label", "label");
    }

    private Label buildLabel(String key, String... styleClasses) {
        Label label = new Label();
        bind(label.textProperty(), key);
        label.getStyleClass().addAll(styleClasses);
        return label;
    }

    private HBox buildHeadingBox(Node... nodes) {
        HBox box = new HBox(nodes);
        box.getStyleClass().add("heading-box");
        return box;
    }

    private VBox buildPage(Header header, VBox body) {
        VBox page = new VBox(header, body);
        page.getStyleClass().add("page");
        return page;
    }

    private VBox buildVBox(String[] styleClasses, Node... nodes) {
        VBox box = new VBox(nodes);
        box.getStyleClass().addAll(styleClasses);
        return box;
    }

    private void addListeners() {
        setOnKeyPressed(key -> {
            if (key.getCode() == KeyCode.ENTER) {
                controller.handleEnterPressed();
            }
        });
    }

    @Override
    public String toString() {
        return getClass().getName();
    }
}