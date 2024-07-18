package javafx.panes;

import console.entities.User;
import javafx.assets.ColorTheme;
import javafx.assets.Header;
import javafx.assets.LanguagePack;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.controllers.SettingsController;
import javafx.eventlisteners.EventListeners;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;

import java.util.Locale;

/**
 * The type Settings pane.
 */
@Getter
public class SettingsPane extends CustomPane {

    private final SettingsController controller;
    private static final Locale[] locales = {
            Locale.GERMAN,
            Locale.ENGLISH
    };

    /**
     * Instantiates a new Settings pane.
     *
     * @param stage          the stage
     * @param eventListeners the event listeners
     * @param user           the user
     */
    public SettingsPane(Stage stage, EventListeners eventListeners, User user) {
        super(stage, eventListeners, user, eventListeners.getColorTheme());
        this.controller = new SettingsController(stage, this, eventListeners, user);
        styleClasses = new String[]{
                "settings.css",
                "header.css"
        };
        build();
    }

    private VBox page;
    private Header header;
    private VBox body;

    private VBox languageChangeBox;
    private Label languageChangeLabel;
    private ChoiceBox<String> languageChoiceBox;

    private VBox appearanceBox;
    private Label appearanceLabel;
    private VBox appearanceChoicesBox;

    private RadioButton whiteModeBox;
    private RadioButton darkModeBox;

    private HBox buttonBox;
    private Button deleteAccountButton;
    private Button insuranceButton;

    @Override
    protected void build() {
        setMinSize(STAGE_WIDTH, STAGE_HEIGHT);
        setVgap(V_GAP);
        addStyleSheets();
        buildNodes();
        add(page, 0, 0);
    }

    @Override
    protected void buildNodes() {
        this.header = new Header(getStage(), "settings.label.title");
        createBody();
        this.page = buildPage(header, body);
    }

    private void createBody() {
        createLanguageChangeBox();
        createAppearanceBox();
        createButtonBox();
        this.body = buildBody(languageChangeBox, appearanceBox, buttonBox);
    }

    private void createButtonBox() {
        this.deleteAccountButton = buildAccDelButton();
        this.insuranceButton = buildInsuranceButton();
        String[] styleClasses = {"button-box"};
        this.buttonBox = buildHBox(styleClasses, deleteAccountButton, insuranceButton);
    }

    private Button buildInsuranceButton() {
        Button button = new Button();
        button.getStyleClass().addAll("insurance-btn", "button");
        bind(button.textProperty(), "settings.button.insurance");
        button.setOnMouseClicked(controller::handleAccountInsurance);
        button.setVisible(false);
        return button;
    }

    private void createLanguageChangeBox() {
        this.languageChangeLabel = buildLanguageChangeLabel();
        this.languageChoiceBox = buildLanguageChoiceBox();
        String[] styleClasses = {"language-change-box"};
        this.languageChangeBox = buildVBox(styleClasses, languageChangeLabel, languageChoiceBox);
    }

    private Label buildLanguageChangeLabel() {
        String[] styleClasses = {"language-change-label", "label"};
        return buildLabel("settings.label.language_change", styleClasses);
    }

    private ChoiceBox<String> buildLanguageChoiceBox() {
        ObservableList<String> options = FXCollections.observableArrayList(LanguagePack.getSupportedLocaleStrings());
        ChoiceBox<String> choiceBox = new ChoiceBox<>(options);
        choiceBox.getSelectionModel().select(0);
        choiceBox.getStyleClass().addAll("language-choice-box");
        choiceBox.getSelectionModel().selectedItemProperty().addListener((observable, oldLanguage, newLanguage) -> controller.handleLanguageChange(newLanguage));
        return choiceBox;
    }

    private void createAppearanceBox() {
        this.appearanceLabel = buildLabel("settings.label.appearance", "appearance-label", "label");
        createAppearanceChoicesBox();
        String[] styleClasses = {"appearance-box"};
        this.appearanceBox = buildVBox(styleClasses, appearanceLabel, appearanceChoicesBox);
    }

    private void createAppearanceChoicesBox() {
        this.whiteModeBox = buildWhiteCheckBox();
        this.darkModeBox = buildDarkCheckBox();
        ToggleGroup group = new ToggleGroup();
        group.getToggles().addAll(whiteModeBox, darkModeBox);
        String[] styleClasses = {"appearance-choices-box"};
        this.appearanceChoicesBox = buildVBox(styleClasses, whiteModeBox, darkModeBox);
    }

    private RadioButton buildWhiteCheckBox() {
        String[] styleClasses = {"white-check-box"};
        RadioButton checkBox = buildCheckBox("settings.label.appearance.white_mode", styleClasses, false);
        checkBox.setOnAction(event -> controller.handleThemeChange(checkBox, ColorTheme.LIGHT));
        return checkBox;
    }

    private RadioButton buildDarkCheckBox() {
        String[] styleClasses = {"dark-check-box"};
        RadioButton checkBox = buildCheckBox("settings.label.appearance.dark_mode", styleClasses, true);
        checkBox.setOnAction(event -> controller.handleThemeChange(checkBox, ColorTheme.DARK));
        return checkBox;
    }

    private RadioButton buildCheckBox(String nameKey, String[] styleClasses, boolean isSelected) {
        RadioButton checkBox = new RadioButton();
        bind(checkBox.textProperty(), nameKey);
        checkBox.getStyleClass().addAll(styleClasses);
        checkBox.setSelected(isSelected);
        return checkBox;
    }

    private Button buildAccDelButton() {
        Button button = new Button();
        button.getStyleClass().addAll("acc-del-btn", "button");
        bind(button.textProperty(), "settings.button.delete_account");
        button.setOnMouseClicked(controller::handleAccountDeletion);
        return button;
    }

    private VBox buildBody(Node... nodes) {
        VBox body = new VBox(nodes);
        body.getStyleClass().add("body");
        return body;
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

}
