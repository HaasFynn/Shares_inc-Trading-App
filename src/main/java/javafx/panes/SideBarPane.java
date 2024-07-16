package javafx.panes;

import console.entities.User;
import javafx.eventlisteners.EventListeners;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import lombok.Getter;

public class SideBarPane extends CustomPane {

    @Getter
    private enum ColorTheme {
        LIGHT("black"), DARK("grey");

        private final String colorPathFragment;

        ColorTheme(String pathFragment) {
            this.colorPathFragment = pathFragment;
        }

    }

    private final EventListeners eventListeners;
    private User user;
    private ColorTheme colorTheme = ColorTheme.DARK;
    private VBox currentImageBox;
    private static final String ICONS_DIR = "assets/images/icons";
    private static final double ICON_WIDTH = 28;
    private static final double SCENE_WIDTH = 60;
    private static final double SCENE_HEIGHT = 500;

    public SideBarPane(Stage stage, EventListeners eventListeners, User user) {
        super(stage, eventListeners, user);
        this.user = user;
        this.eventListeners = eventListeners;
        build();
    }

    VBox body;
    VBox header;
    VBox footer;
    VBox home;
    VBox portfolio;
    VBox trade;
    VBox account;
    VBox settings;

    @Override
    protected void build() {
        setMinSize(SCENE_WIDTH, SCENE_HEIGHT);
        addStyleSheet();
        this.body = buildBody();
        getChildren().add(body);
        getStyleClass().add("page");
    }

    private void addStyleSheet() {
        getStylesheets().add(STYLE_PATH + "sidebar.css");
    }

    private VBox buildBody() {
        VBox box = new VBox();
        box.getStyleClass().add("body");
        createNodes();

        this.header = buildImageContainer(home, portfolio, trade);
        this.footer = buildImageContainer(account, settings);

        box.getChildren().addAll(header, footer);
        return box;
    }

    private void createNodes() {
        this.home = buildImageBox("home.png", new DashboardPane(getStage(), eventListeners, user));
        this.portfolio = buildImageBox("document.png", new ShareCreatorPane(getStage(), eventListeners, user));
        this.trade = buildImageBox("handshake.png", new TradePane(getStage(), eventListeners, user));
        this.account = buildImageBox("profile.png", new ProfilePane(getStage(), eventListeners, user));
        this.settings = buildImageBox("settings.png", new SettingsPane(getStage(), eventListeners, user));
        home.getStyleClass().add("selected-pane");
    }

    private VBox buildImageBox(String image, CustomPane pane) {
        VBox box = new VBox();
        box.getStyleClass().add("image-box");
        box.getChildren().add(buildImageView(image));
        addListener(box, pane);
        return box;
    }

    private ImageView buildImageView(String iconName) {
        ImageView imageView = new ImageView(new Image(getIconPath(iconName), ICON_WIDTH, ICON_WIDTH, false, false, true));
        imageView.getStyleClass().add("image-view");
        return imageView;
    }

    private String getIconPath(String iconName) {
        String color = colorTheme.getColorPathFragment();
        return String.format("%s/%s/%s", ICONS_DIR, color, iconName);
    }

    private VBox buildImageContainer(VBox... viewBoxes) {
        VBox box = new VBox();
        box.getStyleClass().add("image-box");
        box.getChildren().addAll(viewBoxes);
        return box;
    }

    private void addListener(VBox node, CustomPane newPane) {
        node.setOnMouseClicked(event -> {
            getCurrentImageBox().getStyleClass().remove("selected-pane");
            eventListeners.switchPane(newPane);
            node.getStyleClass().add("selected-pane");
        });
    }

    private Node getCurrentImageBox() {
        for (Node node : header.getChildren()) {
            if (node.getStyleClass().contains("selected-pane")) {
                return node;
            }
        }
        for (Node node : footer.getChildren()) {
            if (node.getStyleClass().contains("selected-pane")) {
                return node;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return getClass().getName();
    }
}
