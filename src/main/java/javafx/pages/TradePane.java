package javafx.pages;

import console.entities.User;
import javafx.assets.LanguagePack;
import javafx.assets.NewsBox;
import javafx.assets.ShareInfoBox;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.controllers.TradeController;
import javafx.eventlisteners.EventListeners;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import lombok.Getter;

import java.util.ArrayList;

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
    private static final int START_AMOUNT_OF_SHARES = 25;
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

    private TextField searchField;

    private TableView<ShareInfoBox> searchTableView;
    private TableColumn<ShareInfoBox, String> nameColumn;
    private TableColumn<ShareInfoBox, String> revenueColumn;

    private VBox filterBox;
    private VBox filterHeader;
    private Label filterTitle;
    private VBox filterBody;
    private ListView<CheckBox> filterList;

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
        fillTableList(tableView);

        tableView.getStyleClass().add("search-response-box");
        return tableView;
    }

    private void fillTableList(TableView<ShareInfoBox> tableView) {
        ObservableList<ShareInfoBox> shareInfoBoxes = controller.getShareInfoBoxes(controller.getShares(START_AMOUNT_OF_SHARES));
        for (ShareInfoBox box : shareInfoBoxes) {
            tableView.getItems().add(box);
        }
    }

    private void buildTableColumns() {
        nameColumn = buildColumn("trade.column.share_name", "name");
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
        searchField = buildInputField("trade.search.input.placeholder", "input-field");
        shareSearchBoxHeader = buildShareSearchBoxHeader(searchField);
    }

    private HBox buildShareSearchBoxHeader(TextField inputField) {
        HBox box = new HBox(inputField);
        box.getStyleClass().add("share-search-box-heading");
        return box;
    }

    private TextField buildInputField(String binding, String... styleClasses) {
        TextField textField = new TextField();
        bind(textField.promptTextProperty(), binding);
        textField.getStyleClass().addAll(styleClasses);
        return textField;
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

    private ListView<CheckBox> buildFilterList() {
        ListView<CheckBox> box = new ListView<>();
        box.getStyleClass().add("filter-list");
        box.getItems().addAll(getFilterBoxes());
        return box;
    }

    private ArrayList<CheckBox> getFilterBoxes() {
        ArrayList<CheckBox> boxes = new ArrayList<>();
        controller.getFilterTags().forEach(tag -> {
            CheckBox box = new CheckBox();
            box.setText(tag.getName());
            controller.handleCheckBoxSelectionChange(box);
            boxes.add(box);
        });
        return boxes;
    }

    private VBox buildFilterBoxBody(ListView<CheckBox> list) {
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
        controller.handleTextFieldOnPromptChange(searchField);
    }

}