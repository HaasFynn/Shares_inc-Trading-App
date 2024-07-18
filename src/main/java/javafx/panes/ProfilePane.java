package javafx.panes;

import console.entities.User;
import javafx.assets.Header;
import javafx.assets.InputSection;
import javafx.controllers.ProfileController;
import javafx.eventlisteners.EventListeners;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;

/**
 * The type Profile pane.
 */
@Getter
public class ProfilePane extends CustomPane {
    private static final String STYLEPATH = "style/";
    private static final double IMG_MAX_HEIGHT = 100;
    private final ProfileController controller;

    /**
     * Instantiates a new Profile pane.
     *
     * @param stage          the stage
     * @param eventListeners the event listeners
     * @param user           the user
     */
    public ProfilePane(Stage stage, EventListeners eventListeners, User user) {
        super(stage, eventListeners, user, eventListeners.getColorTheme());
        this.controller = new ProfileController(stage, this, eventListeners, user);
        styleClasses = new String[]{
                "profile.css",
                "input-section.css"
        };

        build();
    }

    private VBox page;
    private Header header;

    private VBox body;

    private HBox headingBox;
    private VBox profileBox;
    private Label profileLabel;
    private VBox profileImageBox;
    private VBox imageSurrounder;
    private ImageView profileImage;
    private HBox imageEditBox;
    private Button deleteIMGButton;
    private Button uploadButton;
    private InputSection usernameInput;

    private InputSection firstNameInput;
    private InputSection lastNameInput;
    private InputSection emailInput;

    private VBox passwordBox;
    private Label passwordInputLabel;
    private PasswordField passwordInput;

    private HBox buttonBox;
    private Button saveButton;
    private Button logoutButton;

    @Override
    protected void build() {
        setMinSize(STAGE_WIDTH, STAGE_HEIGHT);
        setVgap(V_GAP);
        addStyleSheets();
        buildNodes();
        addListeners();
        add(page, 0, 0);
    }

    @Override
    protected void buildNodes() {
        this.header = new Header(getStage(), "profile.label.title");
        createBody();
        this.page = buildPage(header, body);
    }

    private void createBody() {
        createHeadingBox();
        this.firstNameInput = buildFirstnameInput();
        this.lastNameInput = buildLastNameInput();
        this.emailInput = buildEmailInput();
        createPasswordBox();
        createButtonBox();
        this.body = buildBody(headingBox, firstNameInput, lastNameInput, emailInput, passwordBox, buttonBox);
    }

    private void createButtonBox() {
        this.saveButton = buildSaveButton();
        this.logoutButton = buildLogoutButton();
        String[] styleClasses = {"button-box"};
        this.buttonBox = buildHBox(styleClasses, saveButton, logoutButton);
    }

    private Button buildSaveButton() {
        String[] styleClasses = {"save-button"};
        Button button = buildButton(styleClasses, "profile.button.save");
        button.setOnMouseClicked(event -> controller.saveInput());
        return button;
    }

    private Button buildLogoutButton() {
        String[] styleClasses = {"logout-button"};
        Button button = buildButton(styleClasses, "profile.button.logout");
        button.setOnMouseClicked(controller::handleLogoutAction);
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
        String[] styleClasses = {"password-box"};
        this.passwordBox = buildVBox(styleClasses, passwordInputLabel, passwordInput);
    }

    private Label buildPasswordLabel() {
        return buildLabel("profile.label.password", "password-label", "label");
    }

    private InputSection buildFirstnameInput() {
        return new InputSection("profile.input.firstname", controller.user()::getFirstname);
    }

    private InputSection buildLastNameInput() {
        return new InputSection("profile.input.lastname", controller.user()::getLastname);
    }

    private InputSection buildEmailInput() {
        return new InputSection("profile.input.email", controller.user()::getEmail);
    }

    private PasswordField buildPasswordInput() {
        PasswordField passwordField = new PasswordField();
        passwordField.getStyleClass().addAll("password-field");
        return passwordField;
    }

    private void createHeadingBox() {
        createProfileBox();
        this.usernameInput = buildUsernameInput();
        String[] styleClasses = {"heading-box"};
        this.headingBox = buildHBox(styleClasses, profileBox, usernameInput);
    }

    private InputSection buildUsernameInput() {
        return new InputSection("profile.input.username", controller.user()::getUsername);
    }

    private void createProfileBox() {
        this.profileLabel = buildProfileLabel();
        buildProfileImageBox();
        String[] styleClasses = {"profile-box"};
        this.profileBox = buildVBox(styleClasses, profileLabel, profileImageBox);
    }

    private void buildProfileImageBox() {
        buildImageSurrounder();
        createImgEditBox();
        String[] styleClasses = {"image-element-box"};
        this.profileImageBox = buildVBox(styleClasses, imageSurrounder, imageEditBox);
    }

    private void buildImageSurrounder() {
        this.profileImage = buildProfileImage();
        String[] styleClasses = {"image-box"};
        this.imageSurrounder = buildVBox(styleClasses, profileImage);
    }

    private void createImgEditBox() {
        this.uploadButton = buildUploadButton();
        this.deleteIMGButton = buildImgDelButton();
        String[] styleClasses = {"image-edit-box"};
        this.imageEditBox = buildHBox(styleClasses, uploadButton, deleteIMGButton);
    }

    private Button buildUploadButton() {
        String[] styleClasses = {"profile-button-image-upload"};
        Button button = buildButton(styleClasses, "profile.button.upload");
        button.setOnMouseClicked(controller::handleImageUpload);
        return button;
    }

    private Button buildImgDelButton() {
        String[] styleClasses = {"profile-button-image-delete"};
        Button button = buildButton(styleClasses, "profile.button.delete");
        button.setOnMouseClicked(controller::handleImageDeletion);
        return button;
    }

    private ImageView buildProfileImage() {
        ImageView image = new ImageView(controller.getUserProfile());
        image.getStyleClass().add("profile-image");
        image.setFitWidth(IMG_MAX_HEIGHT);
        image.setFitHeight(IMG_MAX_HEIGHT);
        return image;
    }

    private Label buildProfileLabel() {
        return buildLabel("profile.label.image", "profile-pic-label", "label");
    }

    private Label buildLabel(String key, String... styleClasses) {
        Label label = new Label();
        bind(label.textProperty(), key);
        label.getStyleClass().addAll(styleClasses);
        return label;
    }

    private HBox buildHBox(String[] styleClasses, Node... nodes) {
        HBox box = new HBox(nodes);
        box.getStyleClass().addAll(styleClasses);
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

}