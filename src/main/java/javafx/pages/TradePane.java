package javafx.pages;

import console.entities.Share;
import console.entities.User;
import javafx.assets.LanguagePack;
import javafx.assets.NewsBox;
import javafx.assets.ShareInfoBox;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.StringProperty;
import javafx.controllers.TradeController;
import javafx.eventlisteners.EventListeners;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
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
    private final EventListeners eventListeners;
    private final TradeController controller;
    private static final double STAGE_WIDTH = 815;
    private static final double STAGE_HEIGHT = 500;
    private static final String ICONS_DIR = "assets/image/icon";
    private static final double ICON_WIDTH = 10;
    private String username;
    private ColorTheme colorTheme = ColorTheme.DARK;

    public TradePane(Stage stage, EventListeners eventListeners, User user) {
        super(stage);
        this.eventListeners = eventListeners;
        this.username = user.getUsername();
        this.controller = new TradeController(this, user);
        build();
    }

    private VBox page;
    private VBox header;
    private Text title;
    private VBox body;
    private HBox searchBox;

    private VBox shareSearchBox;
    private HBox shareSearchBoxHeader;

    private HBox searchIconBox;

    private TextField inputField;

    private HBox filterIconBox;

    private ListView<ShareInfoBox> searchResponseBox;

    private VBox filterBox;
    private VBox filterBoxHeader;
    private Text filterBoxTitle;
    private VBox filterBoxBody;
    private ListView<String> filterList;

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
        buildHeader();
        createBody();
        page = buildPage(header, body);
    }

    private void buildHeader() {
        title = buildText("trade.text.title", "title");
        header = new VBox(title);
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
        body = buildBody(searchBox, newsBox);
    }

    private void buildNewsBox() {
        newsBox = new NewsBox();
    }

    private void createSearchBox() {
        createShareSearchBox();
        createFilterBox();
        searchBox = buildSearchBox(shareSearchBox, filterBox);
    }

    private void createShareSearchBox() {
        createShareSearchBoxHeader();
        searchResponseBox = buildSearchResponseBox();
        shareSearchBox = buildShareSearchBox(shareSearchBoxHeader, searchResponseBox);
    }

    private ListView<ShareInfoBox> buildSearchResponseBox() {
        ListView<ShareInfoBox> listView = new ListView<>();
        listView.getStyleClass().add("search-response-box");
        return listView;
    }

    private void addSearchResponseBoxListener() {
        searchResponseBox.setOnMouseClicked(event -> {
            ShareInfoBox selectedItem = searchResponseBox.getSelectionModel().getSelectedItem();
            if (selectedItem == null) {
                return;
            }
            Share share = controller.getShare(selectedItem.getShareName());
            eventListeners.switchPane(new ShareOverviewPane(stage, controller.getUser(), share));
        });
    }

    private void createShareSearchBoxHeader() {
        searchIconBox = buildIconBox("lens.png", "icon-box");
        inputField = buildInputField("trade.search.input.placeholder", "input-field");
        filterIconBox = buildIconBox("filter.png", "icon-box");
        shareSearchBoxHeader = buildShareSearchBoxHeader(searchIconBox, inputField, filterIconBox);
    }

    private HBox buildShareSearchBoxHeader(HBox iconBox1, TextField inputField, HBox iconBox2) {
        HBox box = new HBox(iconBox1, inputField, iconBox2);
        box.getStyleClass().add("share-search-box-heading");
        return box;
    }

    private TextField buildInputField(String binding, String... styleClasses) {
        TextField textField = new TextField();
        bind(textField.promptTextProperty(), binding);
        textField.getStyleClass().addAll(styleClasses);
        textField.setOnKeyPressed(event -> searchResponseBox.setItems(controller.getSharesByPrompt(inputField.getText())));
        return textField;
    }

    private HBox buildIconBox(String imageName, String... styleClasses) {
        HBox box = new HBox(buildIcon(imageName));
        box.getStyleClass().addAll(styleClasses);
        return box;
    }

    private ImageView buildIcon(String iconName) {
        ImageView imageView = new ImageView(new Image(getIconPath(iconName), ICON_WIDTH, ICON_WIDTH, false, false, true));
        imageView.getStyleClass().add("icon");
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
        createFilterBoxHeader();
        createFilterBoxBody();
        filterBox = buildFilterBox(filterBoxHeader, filterBoxBody);
    }

    private void createFilterBoxHeader() {
        filterBoxTitle = buildText("trade.filter.text.title", "title");
        filterBoxHeader = buildFilterBoxHeader(filterBoxTitle);
    }

    private VBox buildFilterBoxHeader(Text title) {
        VBox box = new VBox(title);
        box.getStyleClass().add("filter-box-heading");
        return box;
    }

    private void createFilterBoxBody() {
        filterList = buildFilterList();
        filterBoxBody = buildFilterBoxBody(filterList);
    }

    private ListView<String> buildFilterList() {
        ListView<String> listView = new ListView<>();
        listView.getStyleClass().add("filter-list");
        listView.getItems().addAll(controller.getFilterTags());
        return listView;
    }

    private VBox buildFilterBoxBody(ListView<String> list) {
        VBox box = new VBox(list);
        box.getStyleClass().add("filter-box-body");
        return box;
    }

    private HBox buildSearchBox(VBox searchBox, VBox filterBox) {
        HBox box = new HBox(searchBox, filterBox);
        box.getStyleClass().add("search-box");
        return box;
    }

    private VBox buildBody(HBox box1, VBox box2) {
        VBox box = new VBox(box1, box2);
        box.getStyleClass().add("body");
        return box;
    }

    private VBox buildFilterBox(VBox header, VBox body) {
        VBox box = new VBox(header, body);
        box.getStyleClass().addAll("filter-box", "surround-box");
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
        addSearchResponseBoxListener();
    }

}