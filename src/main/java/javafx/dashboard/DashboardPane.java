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
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class DashboardPane extends PaneParent {
    private static final double STAGE_WIDTH = 815;
    private static final double STAGE_HEIGHT = 500;
    private static final Color MONEY_TEXT_COLOR = Color.GREEN;
    private static final Color LABEL_TEXT_COLOR = Color.WHITE;
    private final UserDao userDao;
    private final PortfolioDao portfolioDao;
    private final ShareDao shareDao;
    private final User user;

    public DashboardPane(Stage stage, Font font, User user) {
        super(stage, font);
        EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
        this.userDao = new UserDaoImpl(entityManager);
        this.portfolioDao = new PortfolioDaoImpl(entityManager);
        this.shareDao = new ShareDaoImpl(entityManager);
        this.user = userDao.getByUsername(user.getUsername());
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

    private VBox shareChangesBox;
    private Label shareChangeLabel;
    private ShareInfoBox[] shareInfoBoxes; // 5 fit currently

    @Override
    protected void build() {
        setMinSize(STAGE_WIDTH, STAGE_HEIGHT);
        setVgap(10);
        createNodes();
        addListeners();
        add(page, 0, 0);
        if (stage.isShowing()) {
            adjustWindow();
        }
    }

    private void adjustWindow() {
        stage.getScene().getWindow().setHeight(STAGE_HEIGHT);
        stage.getScene().getWindow().setWidth(STAGE_WIDTH);
        stage.centerOnScreen();
        stage.setResizable(true); //Change Goal: Adjustable without problems
    }

    private void buildHeading() {
        this.title = buildTitle();
        this.welcomeText = buildWelcomeText();
        this.header = buildHeader(title, welcomeText);
    }

    private void createNodes() {
        buildHeading();
        buildBody();
        page = new VBox();
        page.setPadding(new Insets(10, 10, 10, 10));
        page.getChildren().addAll(header, accountBalanceBox);
    }

    private void buildBody() {
        this.accountBalanceLabel = buildLabel("dashboard.label.accountbalance", "p");
        this.accountBalance = buildAccountBalanceText();
        this.valueOfSharesLabel = buildLabel("dashboard.label.valueshares", "p");
        this.valueOfShares = buildValueOfSharesText();

        this.accountBalanceBox = buildAccountBalanceBox(accountBalanceLabel, accountBalance, valueOfSharesLabel, valueOfShares);
    }

    private VBox buildHeader(Text text, Text text1) {
        VBox box = new VBox();
        box.getChildren().addAll(text, text1);
        return box;
    }

    private Text buildTitle() {
        return buildText("dashboard.title", "h1");
    }

    private Text buildWelcomeText() {
        return buildText("dashboard.welcomeText", "h3");
    }

    private Text buildAccountBalanceText() {
        Text text = buildText("", "p");
        text.setFill(MONEY_TEXT_COLOR);
        text.textProperty().set(String.valueOf(user.getAccountBalance()));
        return text;
    }

    private Text buildValueOfSharesText() {
        Text text = buildText("", "p");
        text.setFill(MONEY_TEXT_COLOR);
        text.setText(String.valueOf(getShareValue()));
        return text;
    }

    private VBox buildAccountBalanceBox(Label label, Text text, Label label1, Text text1) {
        VBox box = new VBox();
        box.getChildren().addAll(label, text, label1, text1);
        box.setBackground(new Background(new BackgroundFill(Color.valueOf("343434"), null, null)));
        return box;
    }

    private Text buildText(String key, String... styleClasses) {
        Text text = new Text();
        bind(text.textProperty(), key);
        text.getStyleClass().addAll(styleClasses);
        text.setFont(font);
        return text;
    }

    private void bind(StringProperty text, String key) {
        if (key.isEmpty()) return;
        text.bind(getValueByKey(key));
    }

    private void addListeners() {

    }

    private Label buildLabel(String key, String... styleClasses) {
        Label label = new Label();
        bind(label.textProperty(), key);
        label.getStyleClass().addAll(styleClasses);
        label.setFont(font);
        label.setTextFill(LABEL_TEXT_COLOR);
        return label;
    }

    StringBinding getValueByKey(String key) {
        return LanguagePack.createStringBinding(key);
    }

    private double getShareValue() {
        List<Portfolio> portfolioEntries = portfolioDao.getAllFromUser(user.getId());
        return portfolioEntries.stream().mapToDouble(portfolio -> shareDao.get(portfolio.getShareId()).getPricePerShare() * portfolio.getAmount()).sum();
    }
}
