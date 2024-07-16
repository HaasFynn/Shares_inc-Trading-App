package javafx.panes;

import console.entities.User;
import javafx.assets.Header;
import javafx.assets.LanguagePack;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.controllers.SettingsController;
import javafx.eventlisteners.EventListeners;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;

import java.util.Locale;

@Getter
public class SettingsPane extends CustomPane {

    private final SettingsController controller;
    private static final Locale[] locales = {
            Locale.GERMAN,
            Locale.ENGLISH
    };

    public SettingsPane(Stage stage, EventListeners eventListeners, User user) {
        super(stage, eventListeners, user);
        this.controller = new SettingsController(stage, eventListeners, user);
        build();
    }

    private VBox page;
    private Header header;
    private VBox body;

    private VBox languageChangeBox;
    private Label languageChangeLabel;
    private ChoiceBox<String> languageChoiceBox;

    private Label appearanceLabel;
    private VBox appearanceBox;
    private VBox appearanceChoicesBox;

    private HBox whiteModeBox;
    private Label whiteModeLabel;
    private CheckBox whiteModeCheckbox;

    private HBox darkModeBox;
    private Label darkModeLabel;
    private CheckBox darkModeCheckbox;

    private Button deleteAccountButton;

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
        this.header = new Header();
        createBody();
        this.page = buildPage(header, body);
    }

    private void createBody() {
        createLanguageChangeBox();
        createAppearanceBox();
        this.deleteAccountButton = buildAccDelButton();
        this.body = buildBody(languageChangeBox,/* appearanceBox, */deleteAccountButton);
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

    }

    private Button buildAccDelButton() {
        Button button = new Button();
        button.getStyleClass().addAll("button");
        bind(button.textProperty(), "settings.button.delete_account");
        button.setOnMouseClicked(controller::handleAccountDeletion);
        return button;
    }

    private VBox buildBody(Node... nodes) {
        VBox body = new VBox(nodes);
        body.getStyleClass().add("body");
        return body;
    }

    @Override
    protected void addStyleSheets() {
        getStylesheets().addAll(STYLE_PATH + "settings.css");
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

    @Override
    public String toString() {
        return getClass().getName();
    }
}
