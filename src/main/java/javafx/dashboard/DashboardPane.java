package javafx.dashboard;

import backend.dao.*;
import backend.entities.Portfolio;
import backend.entities.Share;
import backend.entities.User;
import backend.functional.EntityManagement;
import jakarta.persistence.EntityManager;
import javafx.PaneParent;
import javafx.assets.LanguagePack;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.StringProperty;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Getter
public class DashboardPane extends PaneParent {
    private static final double STAGE_WIDTH = 815;
    private static final double STAGE_HEIGHT = 500;
    private static final String MONEY_ENDING_SYMBOL = ".-";
    private static final int SHARE_BOX_AMOUNT = 5;
    private final UserDao userDao;
    private final PortfolioDao portfolioDao;
    private final ShareDao shareDao;
    private final String username;
    private final Random rand = new Random();

    public DashboardPane(Stage stage, User user) {
        super(stage);
        EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
        this.userDao = new UserDaoImpl(entityManager);
        this.portfolioDao = new PortfolioDaoImpl(entityManager);
        this.shareDao = new ShareDaoImpl(entityManager);
        this.username = user.getUsername();
        build();
    }

    private VBox page;

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
    private ShareInfoBox[] shareInfoBoxes; // 5 fit currently

    private VBox newsSurroundingBox;
    private VBox newsBox;
    private Label newsBoxLabel;
    private Text newsText;
    
    @Override
    protected void build() {
        setMinSize(STAGE_WIDTH, STAGE_HEIGHT);
        setVgap(10);
        addStyleSheet();
        createNodes();
        addListeners();
        add(page, 0, 0);
        if (stage.isShowing()) {
            adjustWindow();
        }
    }

    private void addStyleSheet() {
        getStylesheets().addAll("style/dashboard.css", "style/share_info_box.css");
    }

    private void adjustWindow() {
        stage.getScene().getWindow().setHeight(STAGE_HEIGHT);
        stage.getScene().getWindow().setWidth(STAGE_WIDTH);
        stage.centerOnScreen();
        stage.setResizable(true); //Change Goal: Adjustable without problems
    }

    private void buildHeader() {
        this.title = buildTitle();
        this.welcomeText = buildWelcomeText();
        this.header = buildHeader(title, welcomeText);
    }

    private void createNodes() {
        buildHeader();
        buildBody();
        buildPage(header, body);
    }

    private void buildPage(VBox header, HBox box) {
        page = new VBox();
        page.getStyleClass().add("page");
        page.getChildren().addAll(header, box);
    }

    private void buildBody() {
        buildAccBalanceBox();
        buildShareChangeOverview();
        buildNewsBoxPart();
        body = buildBodyBox(accBalanceSurroundingBox, stockMarketSurroundingBox, newsSurroundingBox);
    }

    private HBox buildBodyBox(VBox box1, VBox box2, VBox box3) {
        HBox box = new HBox();
        box.getChildren().addAll(box1, box2, box3);
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

    private void buildNewsBoxPart() {
        this.newsBoxLabel = buildLabel("dashboard.newsbox.label", "label");
        this.newsText = buildNewsText();
        this.newsBox = buildNewsBox(newsBoxLabel, newsText);

        this.newsSurroundingBox = buildSurroundBox(newsBox);
    }

    private Text buildNewsText() {
        Text text = new Text(getRandomNewsText());
        return text;
    }

    private String getRandomNewsText() {
        String nextString;
        ArrayList<String> newsList = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new FileReader("assets/articles.txt"))) {
            while((nextString = br.readLine()) != null) {
                newsList.add(nextString);
            }
        } catch (Exception ignored) {
            return "";
        }
        return newsList.get(rand.nextInt(0) + newsList.size());
    }

    private VBox buildNewsBox(Label label, Text text) {
        VBox box = new VBox();
        box.getChildren().addAll(label, text);
        box.getStyleClass().add("news-box");
        return box;
    }

    private ShareInfoBox[] buildShareInfoBoxes(String... styleClasses) {
        ShareInfoBox[] shareInfoBoxes = new ShareInfoBox[SHARE_BOX_AMOUNT];
        Share[] topShares = getTopShares();
        if (topShares == null) {
            return null;
        }
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
        Share[] shares = shareDao.getAll().toArray(new Share[0]);
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
        String username = userDao.getByUsername(
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
        return userDao.getByUsername(username);
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
        List<Portfolio> portfolioEntries = portfolioDao.getAllFromUser(user().getId());
        return portfolioEntries.stream().mapToDouble(portfolio -> shareDao.get(portfolio.getShareId()).getPricePerShare() * portfolio.getAmount()).sum();
    }
}
