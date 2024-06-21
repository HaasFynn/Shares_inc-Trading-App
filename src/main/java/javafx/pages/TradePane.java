package javafx.pages;

import console.entities.User;
import javafx.assets.LanguagePack;
import javafx.assets.NewsBox;
import javafx.assets.ShareInfoBox;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.StringProperty;
import javafx.controllers.TradeController;
import javafx.eventlisteners.EventListeners;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
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
        this.controller = new TradeController(this, eventListeners, user);
        build();
    }

    private VBox page;
    private VBox header;
    private Label title;
    private VBox body;
    private HBox searchBox;

    private VBox shareSearchBox;
    private HBox shareSearchBoxHeader;

    private HBox searchIconBox;

    private TextField searchField;

    private HBox filterIconBox;

    private TableView<ShareInfoBox> searchTableView;
    private TableColumn<ShareInfoBox, String> nameColumn;
    private TableColumn<ShareInfoBox, String> revenueColumn;

    private VBox filterBox;
    private VBox filterHeader;
    private Label filterTitle;
    private VBox filterBody;
    private ListView<String> filterList; //TODO: Replace with proper way (checkboxes)

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
        title = buildLabel("trade.text.title", "title");
        header = new VBox(title);
        header.getStyleClass().add("header");
    }

    private Label buildLabel(String binding, String... styleClasses) {
        Label label = new Label();
        bind(label.textProperty(), binding);
        label.getStyleClass().addAll(styleClasses);
        return label;
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
        buildTableColumns();
        searchTableView = buildSearchResponseBox(nameColumn, revenueColumn);
        shareSearchBox = buildShareSearchBox(shareSearchBoxHeader, searchTableView);
    }

    private TableView<ShareInfoBox> buildSearchResponseBox(TableColumn<ShareInfoBox, String> nameColumn, TableColumn<ShareInfoBox, String> revenueColumn) {
        TableView<ShareInfoBox> tableView = new TableView<>();
        tableView.getColumns().addAll(nameColumn, revenueColumn);
        tableView.setEditable(true);
        tableView.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        setStandardTableItems(tableView);

        tableView.getStyleClass().add("search-response-box");
        return tableView;
    }

    private void setStandardTableItems(TableView<ShareInfoBox> tableView) {
        tableView.getItems().addAll(
                controller.getShareInfoBoxes(
                        controller.getShares(25)
                )
        );
    }

    private void buildTableColumns() {
        nameColumn = buildColumn("trade.column.share_name", "shareName");
        revenueColumn = buildColumn("trade.column.revenue", "revenue");
    }

    private TableColumn<ShareInfoBox, String> buildColumn(String binding, String propertyName) {
        TableColumn<ShareInfoBox, String> column = new TableColumn<>();
        column.setCellValueFactory(
                new PropertyValueFactory<>(propertyName));
        bind(column.textProperty(), binding);
        return column;
    }

    private void createShareSearchBoxHeader() {
        searchIconBox = buildIconBox("lens.png", "icon-box");
        searchField = buildInputField("trade.search.input.placeholder", "input-field");
        filterIconBox = buildIconBox("filter.png", "icon-box");
        shareSearchBoxHeader = buildShareSearchBoxHeader(searchIconBox, searchField, filterIconBox);
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

    private VBox buildShareSearchBox(HBox box1, TableView<ShareInfoBox> box2) {
        VBox box = new VBox(box1, box2);
        box.getStyleClass().addAll("share-search-box", "surround-box");
        return box;
    }

    private void createFilterBox() {
        createFilterBoxHeader();
        createFilterBoxBody();
        filterBox = buildFilterBox(filterHeader, filterBody);
    }

    private void createFilterBoxHeader() {
        filterTitle = buildLabel("trade.filter.text.title", "body-title");
        filterHeader = buildFilterBoxHeader(filterTitle);
    }

    private VBox buildFilterBoxHeader(Label title) {
        VBox box = new VBox(title);
        box.getStyleClass().add("filter-box-heading");
        return box;
    }

    private void createFilterBoxBody() {
        filterList = buildFilterList();
        filterBody = buildFilterBoxBody(filterList);
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
        controller.handleSearchViewElementSelection(searchTableView);
        controller.handleTextFieldOnEnter(searchField);
    }

}