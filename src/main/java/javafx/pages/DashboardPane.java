package javafx.pages;

import console.entities.Portfolio;
import console.entities.Share;
import console.entities.User;
import javafx.assets.LanguagePack;
import javafx.assets.NewsBox;
import javafx.assets.ShareInfoBox;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.StringProperty;
import javafx.controllers.DashboardController;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;

import java.util.List;
import java.util.Random;
import java.util.stream.IntStream;

@Getter
public class DashboardPane extends CustomPane {
    private static final double STAGE_WIDTH = 815;
    private static final double STAGE_HEIGHT = 500;
    private static final String MONEY_ENDING_SYMBOL = ".-";
    public static final int SHARE_BOX_AMOUNT = 4;
    private final DashboardController controller;
    private final Random rand = new Random();
    public DashboardPane(Stage stage, User user) {
        super(stage);
        this.controller = new DashboardController(stage, this, user);
        build();
    }


    private VBox page;
    private VBox upperPage;

    private VBox header;

    private Text title;
    private Text welcomeText;
    private HBox body;

    private VBox accBalanceSurroundingBox;
    private VBox accBalanceBox;
    private Label accBalanceLabel;
    private Text accBalance;
    private Label valueOfSharesLabel;
    private Text valueOfShares;

    private VBox stockMarketSurroundingBox;
    private VBox stockMarketBox;
    private ListView<ShareInfoBox> stockList;
    private Label marketOverviewLabel;

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

    private void addStyleSheet() {
        getStylesheets().addAll("style/dashboard.css", "style/news_box.css");
    }

    private void buildHeader() {
        this.title = buildTitle();
        this.welcomeText = buildWelcomeText();
        this.header = buildHeader(title, welcomeText);
    }

    private void createNodes() {
        buildUpperPage();
        buildNewsBox();
        buildPage(upperPage, newsBox);
    }

    private void buildNewsBox() {
        this.newsBox = new NewsBox();
    }

    private void buildUpperPage() {
        buildHeader();
        buildBody();

        this.upperPage = new VBox(header, body);
        upperPage.getStyleClass().add("upper-page");
    }

    private void buildPage(VBox header, VBox box2) {
        page = new VBox();
        page.getStyleClass().add("page");
        page.getChildren().addAll(header, box2);
    }

    private void buildBody() {
        buildAccBalanceBox();
        buildShareChangeOverview();
        body = buildBodyBox(accBalanceSurroundingBox, stockMarketSurroundingBox);
    }

    private HBox buildBodyBox(VBox box1, VBox box2) {
        HBox box = new HBox(box1, box2);
        box.getStyleClass().add("body");
        return box;
    }

    private void buildAccBalanceBox() {
        this.accBalanceLabel = buildLabel("dashboard.label.accountbalance", "label");
        this.accBalance = buildAccountBalanceText();
        this.valueOfSharesLabel = buildLabel("dashboard.label.value_shares", "label");
        this.valueOfShares = buildValueOfSharesText();

        this.accBalanceBox = buildAccBalanceBox(accBalanceLabel, accBalance, valueOfSharesLabel, valueOfShares);
        this.accBalanceSurroundingBox = buildSurroundBox(accBalanceBox);
    }

    private void buildShareChangeOverview() {
        this.marketOverviewLabel = buildLabel("dashboard.shareinfo.label", "label");
        this.stockList = buildStockList();
        this.stockMarketBox = buildStockMarketBox(marketOverviewLabel,stockList);

        this.stockMarketSurroundingBox = buildSurroundBox(stockMarketBox);
    }

    private VBox buildStockMarketBox(Label label, ListView<ShareInfoBox> stockList) {
        VBox box = new VBox(label, stockList);
        box.getStyleClass().add("stock-market-box");
        return box;
    }


    private ShareInfoBox[] getShareInfoBoxes() {
        ShareInfoBox[] shareInfoBoxes;
        Share[] topShares = controller.getTopShares(SHARE_BOX_AMOUNT);
        shareInfoBoxes = IntStream.range(0, SHARE_BOX_AMOUNT).mapToObj(i -> new ShareInfoBox(topShares[i].getName())).toArray(ShareInfoBox[]::new);
        return shareInfoBoxes;
    }


    private ListView<ShareInfoBox> buildStockList() {
        ListView<ShareInfoBox> stockList = new ListView<>();
        stockList.setCellFactory(controller.getStockListCellFactory());
        stockList.getItems().addAll(getShareInfoBoxes());
        stockList.getStyleClass().add("stock-list");
        return stockList;
    }

    private VBox buildHeader(Text text, Text text1) {
        VBox box = new VBox();
        box.getStyleClass().add("header");
        box.getChildren().addAll(text, text1);
        return box;
    }

    private Text buildTitle() {
        return buildText("dashboard.title", "title");
    }

    private Text buildWelcomeText() {
        Text text = buildText("", "welcome-text");
        String username = user().getUsername();
        text.textProperty().set(getValueByKey("dashboard.text.welcome").get() + " " + username);
        return text;
    }

    private Text buildAccountBalanceText() {
        Text text = new Text();
        text.textProperty().set(getAccBalance() + MONEY_ENDING_SYMBOL);
        text.getStyleClass().add("money-text");
        return text;
    }

    private String getAccBalance() {
        String accountBalance = "0";
        if (user() != null) {
            accountBalance = String.valueOf(user().getAccountBalance());
        }
        return accountBalance;
    }

    private User user() {
        return controller.getUser();
    }

    private Text buildValueOfSharesText() {
        Text text = new Text();
        text.setText(getShareValue() + MONEY_ENDING_SYMBOL);
        text.getStyleClass().add("money-text");
        return text;
    }

    private VBox buildAccBalanceBox(Label label, Text text, Label label1, Text text1) {
        VBox box = new VBox();
        box.getChildren().addAll(label, text, label1, text1);
        box.getStyleClass().addAll("money-box");
        return box;
    }

    private VBox buildSurroundBox(VBox box1) {
        VBox box = new VBox();
        box1.setAlignment(Pos.CENTER_LEFT);
        box.getChildren().add(box1);
        box.getStyleClass().addAll("surround-box");
        return box;
    }

    private Text buildText(String key, String... styleClasses) {
        Text text = new Text();
        bind(text.textProperty(), key);
        text.getStyleClass().addAll(styleClasses);
        return text;
    }

    private void bind(StringProperty text, String key) {
        if (key.isEmpty()) {
            return;
        }
        text.bind(getValueByKey(key));
    }

    private void addListeners() {

    }

    private Label buildLabel(String key, String... styleClasses) {
        Label label = new Label();
        bind(label.textProperty(), key);
        label.getStyleClass().addAll(styleClasses);
        return label;
    }

    StringBinding getValueByKey(String key) {
        return LanguagePack.createStringBinding(key);
    }

    private double getShareValue() {
        List<Portfolio> portfolioEntries = controller.getUserPortfolio(user().getId());
        return portfolioEntries.stream().mapToDouble(portfolio -> controller.get(portfolio.getShareId()).getPricePerShare() * portfolio.getAmount()).sum();
    }
}
