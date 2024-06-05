package javafx.dashboard;

import backend.dao.*;
import backend.entities.Portfolio;
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

import java.util.List;

@Getter
public class DashboardPane extends PaneParent {
    private static final double STAGE_WIDTH = 815;
    private static final double STAGE_HEIGHT = 500;
    private static final String MONEY_ENDING_SYMBOL = ".-";
    private final UserDao userDao;
    private final PortfolioDao portfolioDao;
    private final ShareDao shareDao;
    private final String username;

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

    private VBox surroundBox;
    private VBox accountBalanceBox;
    private Label accountBalanceLabel;
    private Text accountBalance;
    private Label valueOfSharesLabel;
    private Text valueOfShares;

    private VBox shareChangeBox;
    private Label shareChangeLabel;
    private ShareInfoBox[] shareInfoBoxes; // 5 fit currently

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
        getStylesheets().add("style/dashboard.css");
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
        buildAccountBalanceBox();
        //buildShareInfoBox(); TODO:
        body = buildBodyBox(surroundBox/*, shareChangesBox*/);
    }

    private HBox buildBodyBox(VBox box1/*, VBox box2*/) {
        HBox box = new HBox();
        box.getChildren().addAll(box1/*, box2*/);
        return box;
    }

    private void buildAccountBalanceBox() {
        this.accountBalanceLabel = buildLabel("dashboard.label.accountbalance", "p", "money-label");
        this.accountBalance = buildAccountBalanceText();
        this.valueOfSharesLabel = buildLabel("dashboard.label.valueshares", "p", "money-label");
        this.valueOfShares = buildValueOfSharesText();

        this.accountBalanceBox = buildAccountBalanceBox(accountBalanceLabel, accountBalance, valueOfSharesLabel, valueOfShares);
        this.surroundBox = buildSurroundBox(accountBalanceBox);
    }

    private void buildShareInfoBox() {
        this.shareChangeBox = buildShareChangeBox(shareChangeLabel, shareInfoBoxes);
    }

    private VBox buildShareChangeBox(Label label, ShareInfoBox[] infoBoxes) {
        VBox box = new VBox();
        box.getChildren().add(label);
        addInfoBoxes(box, infoBoxes);
        return box;
    }

    private void addInfoBoxes(VBox box, ShareInfoBox[] infoBoxes) {
        for (ShareInfoBox currentShareInfoBox : infoBoxes) {
            box.getChildren().add(currentShareInfoBox);
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
        Text text = buildText("dashboard.welcomeText", "welcome-text");
        /*String username = userDao.getByUsername(
                user.getUsername())
                .getFirstname();
        text.textProperty().set(text.getText()); // TODO: add username to the welcome text*/
        return text;
    }

    private Text buildAccountBalanceText() {
        Text text = new Text();
        text.textProperty().set(getAccountBalance() + MONEY_ENDING_SYMBOL);
        text.getStyleClass().add("money-text");
        return text;
    }

    private String getAccountBalance() {
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

    private VBox buildAccountBalanceBox(Label label, Text text, Label label1, Text text1) {
        VBox box = new VBox();
        box.getChildren().addAll(label, text, label1, text1);
        box.getStyleClass().addAll("money-box");
        return box;
    }

    private VBox buildSurroundBox(VBox accountBalanceBox) {
        VBox box = new VBox();
        box.getChildren().add(accountBalanceBox);
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
