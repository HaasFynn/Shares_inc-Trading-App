package javafx.panes;

import console.entities.Share;
import console.entities.User;
import javafx.collections.FXCollections;
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
        super(stage, eventListeners, user, eventListeners.getColorTheme());
        this.controller = new StockMarketController(stage, this, eventListeners, share);
        styleClasses = new String[]{
                "stock-market.css"
        };
        build();
    }

    private VBox page;

    private VBox header;
    private Label title;
    private Line line;

    private HBox body;
    private LineChart<String, Number> shareChart;

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
        createHeader();
        createBodyNodes();
        this.page = buildPage(header, body);
    }

    private void createHeader() {
        this.title = buildTitle();
        this.line = buildLine();
        this.header = buildHeader(title, line);
    }

    private VBox buildHeader(Label title, Line line) {
        VBox box = new VBox(title, line);
        box.getStyleClass().add("header");
        return box;
    }

    private Label buildTitle() {
        Label label = new Label(controller.share().getName());
        label.getStyleClass().addAll("h2", "title");
        return label;
    }

    private Line buildLine() {
        Line line = new Line();
        line.setStartX(0);
        line.setStartY(0);
        line.endXProperty().bind(getStage().widthProperty());
        return line;
    }


    private VBox buildPage(VBox header, HBox body) {
        VBox box = new VBox(header, body);
        box.getStyleClass().add("page");
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
        series.nameProperty().bind(getValueByKey("stock_market_pane.chart.share.label.series"));

        controller.generateSampleData();
        fillChartData(series);
        chart.setData(FXCollections.observableArrayList(series));
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

}
