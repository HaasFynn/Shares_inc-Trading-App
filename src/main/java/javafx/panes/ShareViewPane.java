package javafx.panes;

import console.entities.Share;
import console.entities.User;
import javafx.assets.ShareInfoBox;
import javafx.controllers.ShareViewController;
import javafx.eventlisteners.EventListeners;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;

/**
 * The type Share view pane.
 */
@Getter
public class ShareViewPane extends CustomPane {

    private final ShareViewController controller;
    private User user;
    private Share share;
    private static final int MIN_SPINNER_VALUE = 1;
    private static final int MAX_SPINNER_VALUE = 100_000_000;
    private static final int SPINNER_START_VALUE = 1;
    private static final int SPINNER_STEP_SIZE = 1;

    /**
     * Instantiates a new Share view pane.
     *
     * @param stage          the stage
     * @param eventListeners the event listeners
     * @param user           the user
     * @param share          the share
     */
    public ShareViewPane(Stage stage, EventListeners eventListeners, User user, Share share) {
        super(stage, eventListeners, user, eventListeners.getColorTheme());
        this.user = user;
        this.share = share;
        this.controller = new ShareViewController(stage, this, eventListeners, share, user);
        styleClasses = new String[]{
                "share-view.css",
                "header.css"
        };
        build();
    }

    private VBox page;

    private VBox header;
    private Label title;
    private Line headerLine;

    private HBox body;

    private VBox valueBox;
    private VBox informationBox;
    private Text price;
    private Text revenue;
    private VBox descriptionBox;
    private Label descriptionLabel;
    private ScrollPane descriptionScrollPane;
    private Text descriptionText;
    private VBox descriptionTextBox;

    private VBox tradeInformationBox;
    private VBox chartLinkBox;
    private Button chartLinkButton;
    private VBox tradeBox;
    private Spinner<Integer> shareAmountSpinner;
    private HBox buySellBox;
    private Button buyButton;
    private Button sellButton;

    @Override
    protected void build() {
        setMinSize(STAGE_WIDTH, STAGE_HEIGHT);
        setVgap(V_GAP);
        addStyleSheets();
        buildNodes();
        addListeners();
        add(page, 0, 0);
    }

    @Override
    protected void buildNodes() {
        createHeaderNodes();
        createBodyNodes();
        this.page = buildPage(header, body);
    }

    private VBox buildPage(VBox header, HBox body) {
        VBox box = new VBox(header, body);
        box.getStyleClass().add("page");
        return box;
    }

    private void createHeaderNodes() {
        this.title = buildTitle();
        this.headerLine = buildHeaderLine();
        this.header = buildHeader(title, headerLine);
    }

    private Label buildTitle() {
        Label label = new Label(controller.share().getName());
        label.getStyleClass().addAll("title");
        return label;
    }

    private Line buildHeaderLine() {
        Line line = new Line();
        line.setStartX(0);
        line.setStartY(0);
        line.endXProperty().bind(getStage().widthProperty());
        line.getStyleClass().add("line");
        return line;
    }

    private VBox buildHeader(Label title, Line headerLine) {
        VBox box = new VBox(title, headerLine);
        box.getStyleClass().addAll("h1", "title");
        return box;
    }

    private void createBodyNodes() {
        createValueInformationBox();
        createTradeInformationBox();
        this.body = buildHBox(new String[]{"body"}, valueBox, tradeInformationBox);
    }

    private void createValueInformationBox() {
        createInformationBox();
        createDescriptionBox();
        this.valueBox = buildVBox(new String[]{"price-box"}, informationBox, descriptionBox);
    }

    private void createInformationBox() {
        createPriceText();
        createRevenueText();
        this.informationBox = buildVBox(new String[]{"information-box"}, price, revenue);
    }

    private void createPriceText() {
        String pricePerShare = String.valueOf(controller.share().getPricePerShare());
        this.price = buildBoundText("share_view.text.price", pricePerShare, ".-", "info-text", "text");
    }

    private void createRevenueText() {
        ShareInfoBox infoBox = new ShareInfoBox(controller.share());
        this.revenue = buildBoundText("share_view.text.revenue", infoBox.getRevenue().getText(), "", "info-text", "text");
        revenue.setFill(
                infoBox.getRevenue().getFill()
        );
    }

    private Text buildBoundText(String binding, String value, String stringEnding, String... styleClasses) {
        Text text = new Text();
        text.textProperty().set(getValueByKey(binding).get() + " " + value + stringEnding);
        text.getStyleClass().addAll(styleClasses);
        return text;
    }

    private void createDescriptionBox() {
        this.descriptionLabel = buildLabel("share_view.label.description", "description-label", "label");
        createDescriptionScrollPane();
        this.descriptionBox = buildVBox(new String[]{"description-box"}, descriptionLabel, descriptionScrollPane);
    }

    private void createDescriptionScrollPane() {
        createDescriptionTextBox();
        this.descriptionScrollPane = buildDescriptionScrollPane(descriptionTextBox);
    }

    private void createDescriptionTextBox() {
        this.descriptionText = buildDescriptionText();
        this.descriptionTextBox = buildDescriptionTextBox(descriptionText);
    }

    private VBox buildDescriptionTextBox(Text descriptionText) {
        VBox box = new VBox(descriptionText);
        box.setPadding(new Insets(2, 2, 2, 2));
        return box;
    }

    private ScrollPane buildDescriptionScrollPane(VBox textBox) {
        ScrollPane scrollPane = new ScrollPane(textBox);
        scrollPane.getStyleClass().addAll("description-scroll-pane");
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        return scrollPane;
    }

    private Text buildDescriptionText() {
        Text text = new Text();
        text.setWrappingWidth(380);
        bind(text.textProperty(), "share_view.text.sample");
        text.getStyleClass().addAll("description-text", "text");
        return text;
    }

    private void createTradeInformationBox() {
        createChartLinkBox();
        createTradeBox();
        this.tradeInformationBox = buildVBox(new String[]{"trade-information-box"}, chartLinkBox, tradeBox);
    }

    private void createChartLinkBox() {
        this.chartLinkButton = buildChartLinkButton();
        this.chartLinkBox = buildVBox(new String[]{"chartlink-box"}, chartLinkButton);
    }

    private void createTradeBox() {
        this.shareAmountSpinner = buildSpinner();
        createBuySellBox();
        this.tradeBox = buildVBox(new String[]{"trade-box"}, shareAmountSpinner, buySellBox);
    }

    private Spinner<Integer> buildSpinner() {
        Spinner<Integer> spinner = new Spinner<>();
        spinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(MIN_SPINNER_VALUE, MAX_SPINNER_VALUE, SPINNER_START_VALUE, SPINNER_STEP_SIZE));
        spinner.setEditable(true);
        spinner.getStyleClass().addAll("trade-spinner");
        controller.addIntegerFilter(spinner);
        return spinner;
    }

    private void createBuySellBox() {
        createBuyButton();
        createSellButton();
        this.buySellBox = buildHBox(new String[]{"buysell-box"}, buyButton, sellButton);
    }

    private void createBuyButton() {
        this.buyButton = buildTradeButton("share_view.button.buy", "buy-button");
        this.buyButton.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                controller.buy();
            }
        });
    }

    private void createSellButton() {
        this.sellButton = buildTradeButton("share_view.button.sell", "sell-button");
        this.sellButton.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                controller.sell();
            }
        });
    }

    private Button buildTradeButton(String key, String... styleClasses) {
        Button button = new Button();
        bind(button.textProperty(), key);
        button.getStyleClass().addAll(styleClasses);
        return button;
    }

    private Button buildChartLinkButton() {
        Button button = new Button();
        bind(button.textProperty(), "share_view.button.chart_link_button");
        button.getStyleClass().addAll("chartlink-button", "button");
        button.setOnMouseClicked(event -> eventListeners.switchPane(new StockMarketPane(getStage(), eventListeners, share, user)));
        return button;
    }

    private Label buildLabel(String key, String... styleClasses) {
        Label label = new Label();
        bind(label.textProperty(), key);
        label.getStyleClass().addAll(styleClasses);
        return label;
    }

    private VBox buildVBox(String[] styleClasses, Node... nodes) {
        VBox box = new VBox(nodes);
        box.getStyleClass().addAll(styleClasses);
        return box;
    }

    private HBox buildHBox(String[] styleClasses, Node... nodes) {
        HBox box = new HBox(nodes);
        box.getStyleClass().addAll(styleClasses);
        return box;
    }

    private void addListeners() {

    }
}
