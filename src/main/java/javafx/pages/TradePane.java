package javafx.pages;

import backend.entities.User;
import javafx.CustomPane;
import javafx.assets.NewsBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class TradePane extends CustomPane {

    private static final double STAGE_WIDTH = 815;
    private static final double STAGE_HEIGHT = 500;

    public TradePane(Stage stage, User user) {
        super(stage);

    }

    private VBox page;
    private VBox body;
    private HBox searchBox;
    private ScrollPane shareSearch;
    private ScrollPane filterList;

    private NewsBox newsBox;

    @Override
    protected void build() {
        setMinSize(STAGE_WIDTH, STAGE_HEIGHT);
        setVgap(10);
        addStyleSheet();
        createNodes();
        addListeners();
        add(page, 0, 0);
    }

    private void createNodes() {
        createBody();
        this.page = buildPage(body, searchBox);
    }

    private void createBody() {
        createSearchBox();
        this.newsBox = new NewsBox();

        this.body = buildBody(searchBox, newsBox);
    }

    private void createSearchBox() {

    }

    private VBox buildBody(HBox box1, VBox box2) {
        VBox box = new VBox(box1, box1);
        box.getStyleClass().add("body");
        return box;
    }

    private VBox buildPage(VBox body, HBox searchBox) {
        VBox box = new VBox(body, searchBox);
        box.getStyleClass().add("trade-pane");
        return box;
    }

    private void addStyleSheet() {
        getStylesheets().add("trade_pane.css");
    }

    private void addListeners() {

    }
}
