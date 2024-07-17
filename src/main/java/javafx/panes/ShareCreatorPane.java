package javafx.panes;

import console.entities.User;
import javafx.eventlisteners.EventListeners;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.controllers.ShareCreatorController;
import javafx.stage.Stage;
import lombok.Getter;

@Getter
public class ShareCreatorPane extends CustomPane {
    private final ShareCreatorController controller;
    public static final double STAGE_WIDTH = 815;
    public static final double STAGE_HEIGHT = 500;
    private static final double TEXTFIELD_MIN_WIDTH = 320;
    private static final double TEXTFIELD_MIN_HEIGHT = 23;
    private static final double BUTTON_WIDTH = 80;
    private static final double BUTTON_HEIGHT = 23;
    private static final double STANDARD_PADDING = 20;

    public ShareCreatorPane(Stage stage, EventListeners eventListeners, User user) {
        super(stage, eventListeners, user, eventListeners.getColorTheme());
        this.controller = new ShareCreatorController(stage, this, eventListeners);
        styleClasses = new String[]{};

        build();
    }

    private VBox body;
    private Text title;
    private VBox inputBox;
    private Label inputLabel;
    private TextField inputField;
    private Button submitButton;
    private Text statusText;

    @Override
    protected void build() {
        setMinSize(STAGE_WIDTH, STAGE_HEIGHT);
        setVgap(V_GAP);
        buildNodes();
        addStyleSheets();
        addListeners();
        add(body, 0, 0);
        if (getStage().isShowing()) {
            adjustWindow();
        }
    }

    @Override
    protected void buildNodes() {
        title = buildTitle();
        inputLabel = buildLabel("share.creator.label.input", "p");
        inputField = buildInputField();
        submitButton = buildSubmitButton();
        statusText = buildStatusText();

        inputBox = buildInputBox(inputLabel, inputField, submitButton, statusText);
        body = buildBody(title, inputBox);
    }

    /**
     *
     */
    @Override
    protected void addStyleSheets() {
    }

    private void adjustWindow() {
        getStage().getScene().getWindow().setHeight(STAGE_HEIGHT);
        getStage().getScene().getWindow().setWidth(STAGE_WIDTH);
        getStage().centerOnScreen();
        getStage().setResizable(false);
    }

    private VBox buildBody(Text text, VBox box1) {
        VBox box = new VBox();
        box.setSpacing(10);
        box.setPadding(new Insets(20, 20, 20, 20));
        box.getChildren().addAll(text, box1);
        return box;
    }

    private Text buildTitle() {
        Text text = new Text();
        text.textProperty().bind(getValueByKey("share.creator.text.title"));
        text.getStyleClass().addAll("h1", "strong");
        return text;
    }

    private VBox buildInputBox(Label label, TextField field, Button button, Text text) {
        VBox box = new VBox();
        box.setSpacing(5);
        box.getChildren().addAll(label, field, button, text);
        return box;
    }

    private Label buildLabel(String key, String... styleClasses) {
        Label label = new Label();
        label.textProperty().bind(getValueByKey(key));
        label.getStyleClass().addAll(styleClasses);
        return label;
    }

    private TextField buildField(String key, String... styleClasses) {
        TextField field = new TextField();
        field.promptTextProperty().bind(getValueByKey(key));
        field.getStyleClass().addAll(styleClasses);
        return field;
    }

    private Button buildSubmitButton() {
        Button button = buildButton("share.creator.button.submit", "btn-sm", "btn-success");
        button.setOnMouseClicked(event -> controller.handleOnEnter());
        return button;
    }

    private TextField buildInputField() {
        TextField field = buildField("share.creator.label.input", "p");
        field.textProperty().addListener((observable, oldValue, newValue) -> controller.handleInputValidation(oldValue, newValue));
        return field;
    }

    private Button buildButton(String key, String... styleClasses) {
        Button button = new Button();
        button.textProperty().bind(getValueByKey(key));
        button.getStyleClass().addAll(styleClasses);
        return button;
    }

    private Text buildStatusText() {
        Text text = new Text();
        text.getStyleClass().addAll("p");
        text.setVisible(false);
        return text;
    }

    private void addListeners() {
        setOnKeyPressed((KeyEvent event) -> {
            if (event.getCode() == KeyCode.ENTER) {
                controller.handleOnEnter();
            }
            event.consume();
        });
    }
}