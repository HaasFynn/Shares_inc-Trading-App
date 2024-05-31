package javafx.side_bar;

import javafx.PaneParent;
import javafx.geometry.Insets;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import lombok.Getter;

public class SideBarPane extends PaneParent {

    @Getter
    private enum ColorTheme {
        LIGHT("black"),
        DARK("grey");

        private final String colorPathFragment;

        ColorTheme(String pathFragment) {
            this.colorPathFragment = pathFragment;
        }

    }

    private ColorTheme colorTheme = ColorTheme.DARK;
    private static final String ICONS_DIR = "image/icon";
    private static final double STAGE_WIDTH = 60;
    private static final double STAGE_HEIGHT = 500;
    private static final double BODY_SPACING = 200;
    private static final double ICON_SPACING = 20;
    private static final double ICON_WIDTH = 24;
    private static final double PADDING_TOP = ICON_WIDTH / 2;

    public SideBarPane(Stage stage, Font font) {
        super(stage, font);
        build();
    }

    @Override
    protected void build() {
        setMinSize(STAGE_WIDTH, STAGE_HEIGHT);
        VBox body = buildBody();
        getChildren().add(body);
        setStyle("-fx-background-color: #343434");
    }

    private VBox buildBody() {
        VBox box = new VBox();
        box.setSpacing(BODY_SPACING);
        box.setPadding(new Insets(PADDING_TOP, 0, 0, getCenterDistance()));

        ImageView home = getImageView("home.png");
        ImageView portfolio = getImageView("portfolio.png");
        ImageView trade = getImageView("handshake.png");
        ImageView statistic = getImageView("diagram.png");
        ImageView account = getImageView("account.png");
        ImageView settings = getImageView("settings.png");
        addListeners();

        VBox header = buildImageContainer(home, portfolio, trade, statistic);
        VBox footer = buildImageContainer(account, settings);

        box.getChildren().addAll(header, footer);
        return box;
    }


    private ImageView getImageView(String iconName) {
        ImageView imageView = new ImageView(new Image(getIconPath(iconName)));
        imageView.setFitHeight(ICON_WIDTH);
        imageView.setFitWidth(ICON_WIDTH);
        return imageView;
    }

    private String getIconPath(String iconName) {
        String color = colorTheme.getColorPathFragment();
        return String.format("%s/%s/%s", ICONS_DIR, color, iconName);
    }

    private VBox buildImageContainer(ImageView... views) {
        VBox box = new VBox();
        box.setSpacing(ICON_SPACING);
        box.getChildren().addAll(views);
        return box;
    }

    private void addListeners() {

    }

    private double getCenterDistance() {
        return (60.0 - ICON_WIDTH) / 2.0;
    }
}
