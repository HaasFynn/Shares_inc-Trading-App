package javafx.pages;

import backend.entities.User;
import javafx.assets.LanguagePack;
import javafx.assets.NewsBox;
import javafx.assets.ShareInfoBox;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.StringProperty;
import javafx.controllers.TradeController;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lombok.Getter;

@Getter
public class TradePane extends CustomPane {

    @Getter
    private enum ColorTheme {
        LIGHT("black"), DARK("grey");

        private final String colorPathFragment;

        ColorTheme(String pathFragment) {
            this.colorPathFragment = pathFragment;
        }

    }

    private final TradeController controller;
    private static final double STAGE_WIDTH = 815;
    private static final double STAGE_HEIGHT = 500;
    private static final String ICONS_DIR = "assets/image/icon";
    private static final double ICON_WIDTH = 10;
    private ColorTheme colorTheme = ColorTheme.DARK;

    public TradePane(Stage stage, User user) {
        super(stage);
        this.controller = new TradeController(this, user);
        build();
    }

    private VBox page;
    private VBox header;
    private Text title;
    private VBox body;
    private HBox searchBox;

    private VBox shareSearchBox;
    private HBox shareSearchBoxHeading;
    private ImageView searchIcon;
    private TextField inputField;
    private Button searchButton;
    private ImageView filterIcon;
    private ListView<ShareInfoBox> searchResponseBox;

    private VBox filterBox;
    private VBox filterBoxHeading;
    private Text filterBoxTitle;
    private VBox filterBoxBody;
    private ScrollPane filterList;

    private NewsBox newsBox;

    //TODO: Add Buying Page of Share

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
        buildHeader();
        createBody();
        this.page = buildPage(header, body);
    }

    private void buildHeader() {
        this.title = buildText("trade.text.title", "title");
        this.header = new VBox(title);
        header.getStyleClass().add("header");
    }

    private Text buildText(String binding, String... styleClasses) {
        Text text = new Text();
        bind(text.textProperty(), binding);
        text.getStyleClass().addAll(styleClasses);
        return text;
    }

    private void createBody() {
        createSearchBox();
        buildNewsBox();
        this.body = buildBody(searchBox, newsBox);
    }

    private void buildNewsBox() {
        this.newsBox = new NewsBox();
    }

    private void createSearchBox() {
        createShareSearchBox();
        createFilterBox();
        this.searchBox = buildSearchBox(shareSearchBox, null /*filterBox*/);
    }

    private void createShareSearchBox() {
        createShareSearchBoxHeading();
        this.searchResponseBox = buildSearchResponseBox();
        this.shareSearchBox = buildShareSearchBox(shareSearchBoxHeading, searchResponseBox);
    }

    private ListView<ShareInfoBox> buildSearchResponseBox() {
        ListView<ShareInfoBox> listView = new ListView<>();
        listView.getStyleClass().add("search-response-box");
        return listView;
    }

    private void createShareSearchBoxHeading() {
        this.searchIcon = buildIcon("lens.png"); //TODO: Add Icon
        this.inputField = buildInputField("trade.sharesearch.inputfield.placeholder", "input-field");
        this.filterIcon = buildIcon("filter.png"); //TODO: Add Icon
        this.shareSearchBoxHeading = buildShareSearchBoxHeading(searchIcon, inputField, filterIcon);
    }

    private HBox buildShareSearchBoxHeading(ImageView icon1, TextField inputField, ImageView icon2) {
        HBox box = new HBox(icon1, inputField, icon2);
        box.getStyleClass().add("share-search-box-heading");
        return box;
    }

    private TextField buildInputField(String binding, String... styleClasses) {
        TextField textField = new TextField();
        bind(textField.promptTextProperty(), binding);
        textField.getStyleClass().addAll(styleClasses);
        textField.setOnKeyPressed(event ->
                this.searchResponseBox.setItems(controller.getListViewItems(inputField.getText()))
        );
        return textField;
    }

    private ImageView buildIcon(String iconName) {
        ImageView imageView = new ImageView(new Image(getIconPath(iconName), ICON_WIDTH, ICON_WIDTH, false, false, true));
        imageView.getStyleClass().add("image-view");
        return imageView;
    }

    private String getIconPath(String iconName) {
        String color = colorTheme.getColorPathFragment();
        return String.format("%s/%s/%s", ICONS_DIR, color, iconName);
    }

    private VBox buildShareSearchBox(HBox box1, ListView<ShareInfoBox> box2) {
        VBox box = new VBox(box1, box2);
        box.getStyleClass().addAll("share-search-box", "surround-box");
        return box;
    }

    private void createFilterBox() {
    }

    private HBox buildSearchBox(VBox box1, VBox box2) {
        HBox box = new HBox(box1/*, box2*/);
        box.getStyleClass().add("search-box");
        return box;
    }

    private VBox buildBody(HBox box1, VBox box2) {
        VBox box = new VBox(box1, box2);
        box.getStyleClass().add("body");
        return box;
    }

    private VBox buildPage(VBox box1, VBox box2) {
        VBox box = new VBox(box1, box2);
        box.getStyleClass().add("page");
        return box;
    }

    private void addStyleSheet() {
        getStylesheets().addAll("style/trade.css", "style/news_box.css");
    }

    private void bind(StringProperty text, String key) {
        if (key.isEmpty()) {
            return;
        }
        text.bind(getValueByKey(key));
    }

    public StringBinding getValueByKey(String key) {
        return LanguagePack.createStringBinding(key);
    }

    private void addListeners() {

    }
}