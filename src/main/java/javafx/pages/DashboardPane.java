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
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;

import java.util.List;
import java.util.Random;

@Getter
public class DashboardPane extends CustomPane {
    private static final double STAGE_WIDTH = 815;
    private static final double STAGE_HEIGHT = 500;
    private static final String MONEY_ENDING_SYMBOL = ".-";
    private static final int SHARE_BOX_AMOUNT = 5;
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
    private Label marketOverviewLabel;
    private ShareInfoBox[] shareInfoBoxes; /* 5 fit currently*/

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
        this.valueOfSharesLabel = buildLabel("dashboard.label.valueshares", "label");
        this.valueOfShares = buildValueOfSharesText();

        this.accBalanceBox = buildAccBalanceBox(accBalanceLabel, accBalance, valueOfSharesLabel, valueOfShares);
        this.accBalanceSurroundingBox = buildSurroundBox(accBalanceBox);
    }

    private void buildShareChangeOverview() {
        this.marketOverviewLabel = buildLabel("dashboard.shareinfo.label", "label");
        this.shareInfoBoxes = buildShareInfoBoxes("share-info-box");
        this.stockMarketBox = buildShareChangeBox(marketOverviewLabel, shareInfoBoxes);

        this.stockMarketSurroundingBox = buildSurroundBox(stockMarketBox);
    }


    private ShareInfoBox[] buildShareInfoBoxes(String... styleClasses) {
        ShareInfoBox[] shareInfoBoxes = new ShareInfoBox[SHARE_BOX_AMOUNT];
        Share[] topShares = getTopShares();
        for (int i = 0; i < SHARE_BOX_AMOUNT; i++) {
            shareInfoBoxes[i] = new ShareInfoBox(
                    topShares[i].getName(),
                    getRandomRevenue(),
                    rand.nextBoolean()
            );
            shareInfoBoxes[i].getStyleClass().addAll(styleClasses);
        }
        return shareInfoBoxes;
    }

    private double getRandomRevenue() {
        return Math.round(rand.nextDouble(1, 8) * 100) / 100.0;
    }

    private Share[] getTopShares() {
        Share[] shares = controller.shareDao.getAll().toArray(new Share[0]);
        Share[] topShares = new Share[SHARE_BOX_AMOUNT];
        System.arraycopy(shares, 0, topShares, 0, SHARE_BOX_AMOUNT);
        return topShares;
    }

    private VBox buildShareChangeBox(Label label, ShareInfoBox[] infoBoxes) {
        VBox box = new VBox();
        box.getChildren().add(label);
        addInfoBoxes(box, infoBoxes);
        box.getStyleClass().add("stock-market-box");
        return box;
    }

    private void addInfoBoxes(VBox box, ShareInfoBox[] infoBoxes) {
        for (ShareInfoBox infoBox : infoBoxes) {
            box.getChildren().add(infoBox);
        }
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
        String username = controller.userDao.getByUsername(
                        user().getUsername())
                .getFirstname();
        text.textProperty().set(getValueByKey("dashboard.welcomeText").get() + " " + username);
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
        List<Portfolio> portfolioEntries = controller.portfolioDao.getAllFromUser(user().getId());
        return portfolioEntries.stream().mapToDouble(portfolio -> controller.shareDao.get(portfolio.getShareId()).getPricePerShare() * portfolio.getAmount()).sum();
    }
}
