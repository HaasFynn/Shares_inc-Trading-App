package javafx.pages;

import console.entities.Share;
import console.entities.User;
import javafx.assets.LanguagePack;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.StringProperty;
import javafx.controllers.ShareViewController;
import javafx.eventlisteners.EventListeners;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import lombok.Getter;

@Getter
public class ShareViewPane extends CustomPane {

    private final ShareViewController controller;
    private User user;
    private Share share;

    public ShareViewPane(Stage stage, EventListeners eventListeners, User user, Share share) {
        super(stage, eventListeners, user);
        this.user = user;
        this.share = share;
        this.controller = new ShareViewController(this, share, user);

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
    private VBox descriptionSurroundBox;
    private ScrollPane descriptionScrollPane;
    private Text descriptionText;

    private VBox tradeInformationBox;
    private Button chartLinkButton;
    private VBox tradeBox;
    private Spinner<Integer> shareAmountSpinner;
    private HBox buySellBox;
    private Button buyButton;
    private Button sellButton;

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
        label.getStyleClass().add("title");
        return label;
    }

    private Line buildHeaderLine() {
        Line line = new Line();
        line.setStartX(0);
        line.setStartY(0);
        line.endXProperty().bind(getStage().widthProperty());
        return line;
    }

    private VBox buildHeader(Label title, Line headerLine) {
        VBox box = new VBox(title, headerLine);
        box.getStyleClass().addAll("header");
        return box;
    }

    private void createBodyNodes() {
        createValueInformationBox();
        //createTradeInformationBox();
        this.body = buildHBox(new String[]{"body"}, valueBox/*, tradeInformationBox*/);
    }

    private void createValueInformationBox() {
        createInformationBox();
        createDescriptionBox();
        this.valueBox = buildVBox(new String[]{"value-box"}, informationBox, descriptionBox);
    }

    private void createInformationBox() {
        createPriceText();
        createRevenueText();
        this.informationBox = buildVBox(new String[]{"information-box"}, price, revenue);
    }

    private void createPriceText() {
        String pricePerShare = String.valueOf(controller.share().getPricePerShare());
        this.price = buildBoundText("share_view.text.price", pricePerShare, ".-", "text");
    }

    private void createRevenueText() {
        String shareRevenue = String.valueOf(controller.share().getRevenue());
        this.revenue = buildBoundText("share_view.text.revenue", shareRevenue, "%", "text");
    }

    private void createDescriptionBox() {
        this.descriptionLabel = buildLabel("share_view.label.description", "description-label", "label");
        createDescriptionTextBox();
        this.descriptionBox = buildVBox(new String[]{"description-box"}, descriptionLabel, descriptionSurroundBox);
    }

    private void createDescriptionTextBox() {
        createDescriptionScrollPane();
        this.descriptionSurroundBox = buildVBox(new String[]{"description-text-box"}, descriptionScrollPane);
    }

    private void createDescriptionScrollPane() {
        this.descriptionText = buildDescriptionText();
        this.descriptionScrollPane = buildDescriptionScrollPane(descriptionText);
    }

    private ScrollPane buildDescriptionScrollPane(Text text) {
        ScrollPane scrollPane = new ScrollPane(text);
        scrollPane.getStyleClass().addAll("description-scroll-pane");
        return scrollPane;
    }

    private Text buildDescriptionText() {
        Text text = new Text();
        text.setWrappingWidth(380);
        bind(text.textProperty(), "share_view.text.sample");
        text.getStyleClass().addAll("description-text", "text");
        return text;
    }

    private Label buildLabel(String key, String... styleClasses) {
        Label label = new Label();
        bind(label.textProperty(), key);
        label.getStyleClass().addAll(styleClasses);
        return label;
    }

    private Text buildBoundText(String binding, String value, String stringEnding, String... styleClasses) {
        Text text = new Text();
        text.textProperty().set(getValueByKey(binding).get() + " " + value + stringEnding);
        text.getStyleClass().addAll(styleClasses);
        return text;
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

    StringBinding getValueByKey(String key) {
        return LanguagePack.createStringBinding(key);
    }

    private void bind(StringProperty stringProperty, String key) {
        stringProperty.bind(getValueByKey(key));
    }

    private void addListeners() {

    }

    private void addStyleSheet() {
        getStylesheets().addAll(STYLE_PATH + "share-view.css");
    }

    @Override
    public String toString() {
        return getClass().getName();
    }
}
