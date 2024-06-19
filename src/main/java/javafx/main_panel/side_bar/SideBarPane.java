package javafx.main_panel.side_bar;

import backend.entities.User;
import javafx.pages.CustomPane;
import javafx.main_panel.SideBarEventListeners;
import javafx.main_panel.SideBarEventListenersImpl;
import javafx.pages.*;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;

public class SideBarPane extends CustomPane {

    @Getter
    private enum ColorTheme {
        LIGHT("black"),
        DARK("grey");

        private final String colorPathFragment;

        ColorTheme(String pathFragment) {
            this.colorPathFragment = pathFragment;
        }

    }
    private final SideBarEventListeners eventListeners;
    private User user;
    private ColorTheme colorTheme = ColorTheme.DARK;
    private static final String ICONS_DIR = "assets/image/icon";
    private static final double ICON_WIDTH = 28;
    private static final double SCENE_WIDTH = 60;
    private static final double SCENE_HEIGHT = 500;

    public SideBarPane(Stage stage, User user, SideBarEventListenersImpl eventListeners) {
        super(stage);
        this.user = user;
        this.eventListeners = eventListeners;
        build();
    }

    VBox body;
    HBox home;
    HBox portfolio;
    HBox trade;
    HBox statistic;
    HBox account;
    HBox settings;

    @Override
    protected void build() {
        setMinSize(SCENE_WIDTH, SCENE_HEIGHT);
        addStyleSheet();
        this.body = buildBody();
        getChildren().add(body);
        getStyleClass().add("page");
    }

    private void addStyleSheet() {
        getStylesheets().add("style/sidebar.css");
    }

    private VBox buildBody() {
        VBox box = new VBox();
        box.getStyleClass().add("body");
        createNodes();

        VBox header = buildImageContainer(home, portfolio, trade, statistic);
        VBox footer = buildImageContainer(account, settings);

        box.getChildren().addAll(header, footer);
        return box;
    }

    private void createNodes() {
        this.home = buildImageBox("home.png", new DashboardPane(stage, user));
        this.portfolio = buildImageBox("portfolio.png", new PortfolioPane(stage, user));
        this.trade = buildImageBox("handshake.png", new TradePane(stage, user));
        this.statistic = buildImageBox("diagram.png", new StockMarketPane(stage, user));
        this.account = buildImageBox("account.png", new ProfilePane(stage, user));
        this.settings = buildImageBox("settings.png", new SettingsPane(stage, user));
    }

    private HBox buildImageBox(String image, CustomPane pane) {
        HBox box = new HBox();
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

    private VBox buildImageContainer(HBox... viewBoxes) {
        VBox box = new VBox();
        box.getStyleClass().add("image-box-container");
        box.getChildren().addAll(viewBoxes);
        return box;
    }

    private void addListener(Node node, CustomPane pane) {
        node.setOnMouseClicked(event -> eventListeners.handleIconClick(pane));
    }

}
