package javafx.pages;

import console.entities.User;
import javafx.assets.Header;
import javafx.assets.InputSection;
import javafx.assets.LanguagePack;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.StringProperty;
import javafx.controllers.ProfileController;
import javafx.eventlisteners.EventListeners;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.util.concurrent.Callable;

public class ProfilePane extends CustomPane {

    private final ProfileController controller;

    public ProfilePane(Stage stage, EventListeners eventListeners, User user) {
        super(stage, eventListeners, user);
        this.controller = new ProfileController(stage, eventListeners);
    }

    private VBox page;
    private Header header;

    private VBox body;

    private HBox profileBox;
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

    private void createNodes() {
        this.header = new Header();
        createBody();
        this.page = buildPage(header, body);
    }

    private void createBody() {
        createProfileBox();
        this.firstNameInput = buildFirstnameInput();
        this.lastNameInput = buildLastNameInput();
        this.emailInput = buildEmailInput();
        createPasswordBox();
        this.body = buildBody(profileBox, firstNameInput, lastNameInput, emailInput, passwordBox, saveButton);
    }

    private VBox buildBody(Node... nodes) {
        VBox body = new VBox(nodes);
        body.getStyleClass().add("body");
        return body;
    }

    private void createPasswordBox() {
        this.passwordInput = buildPasswordInput();
    }

    private InputSection buildFirstnameInput() {
        return buildInputSection("profile_pane.input.firstname", controller.user().getFirstname());
    }

    private InputSection buildLastNameInput() {
        return buildInputSection("profile_pane.input.lastname", controller.user().getLastname());
    }

    private InputSection buildEmailInput() {
        return buildInputSection("profile_pane.input.email", controller.user().getEmail());
    }

    private PasswordField buildPasswordInput() {
        PasswordField passwordField = new PasswordField();
        passwordField.getStyleClass().addAll("password-field");
        return passwordField;
    }

    private InputSection buildInputSection(String labelBinding, String fieldValue) {
        InputSection section = new InputSection(labelBinding, fieldValue);
        bind(section.getLabel().textProperty(), labelBinding);
        section.getInput().setText(fieldValue);
        return null;
    }

    private void createProfileBox() {
        this.
    }

    private VBox buildPage(Header header, VBox body) {
        VBox page = new VBox(header, body);
        page.getStyleClass().add("page");
        return page;
    }

    private void addStyleSheet() {

    }

    private void addListeners() {

    }

    @Override
    public String toString() {
        return getClass().getName();
    }
}