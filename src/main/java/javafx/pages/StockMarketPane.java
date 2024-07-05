package javafx.pages;

import console.entities.Share;
import console.entities.User;
import javafx.assets.LanguagePack;
import javafx.beans.binding.StringBinding;
import javafx.beans.property.StringProperty;
import javafx.controllers.StockMarketController;
import javafx.eventlisteners.EventListeners;
import javafx.scene.Node;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.stage.Stage;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

public class StockMarketPane extends CustomPane {

    private final StockMarketController controller;

    public StockMarketPane(Stage stage, EventListeners eventListeners, Share share, User user) {
        super(stage, eventListeners, user);
        this.controller = new StockMarketController(stage, this, eventListeners, share);
        build();
    }

    private VBox page;

    private VBox header;
    private Label title;
    private Line headerLine;

    private HBox body;
    private LineChart<String, Number> shareChart;

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
        getStyleClass().addAll(STYLE_PATH + "stock-market-pane.css");
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
        this.shareChart = buildShareChart();
        this.body = buildHBox(new String[]{"body"}, shareChart);
    }

    private LineChart<String, Number> buildShareChart() {
        CategoryAxis xAxis = getDateAxis();
        NumberAxis yAxis = getPriceAxis();
        LineChart<String, Number> chart = new LineChart<>(xAxis, yAxis);

        XYChart.Series<String, Number> series = new XYChart.Series<>();
        series.nameProperty().bind(getValueByKey("stock_market_pane.chart.share.series.label"));

        controller.generateSampleData();
        fillChartData(series);
        chart.getData().addAll(series);
        return chart;
    }

    private NumberAxis getPriceAxis() {
        NumberAxis yAxis = new NumberAxis();
        yAxis.labelProperty().bind(getValueByKey("stock_market_pane.chart.share.price"));
        return yAxis;
    }

    private CategoryAxis getDateAxis() {
        CategoryAxis xAxis = new CategoryAxis();
        xAxis.labelProperty().bind(getValueByKey("stock_market_pane.chart.share.date"));
        return xAxis;
    }

    private void fillChartData(XYChart.Series<String, Number> series) {
        CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                .setHeader("date", "price")
                .setSkipHeaderRecord(true)
                .build();
        try {
            Reader in = new FileReader("C:/Users/fhaas/Documents/Ergon/JavaFx/Shares-inc.-Trading-App/src/main/resources/assets/mocks/price_over_time.csv");
            Iterable<CSVRecord> records = csvFormat.parse(in);
            for (CSVRecord record : records) {
                String date = record.get(0);
                Double price = Double.parseDouble(record.get(1));
                series.getData().add(new XYChart.Data<>(date, price));
            }
        } catch (IOException e) {
            System.err.println("File not found!");
        }
    }

    private HBox buildHBox(String[] styleClasses, Node... nodes) {
        HBox box = new HBox(nodes);
        box.getStyleClass().addAll(styleClasses);
        return box;
    }

    private void addListeners() {

    }

    private void bind(StringProperty stringProperty, String key) {
        stringProperty.bind(getValueByKey(key));
    }

    StringBinding getValueByKey(String key) {
        return LanguagePack.createStringBinding(key);
    }

    @Override
    public String toString() {
        return getClass().getName();
    }
}
