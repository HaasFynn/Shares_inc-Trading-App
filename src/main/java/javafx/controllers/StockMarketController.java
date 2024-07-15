package javafx.controllers;

import console.dao.*;
import console.entities.Share;
import console.functional.EntityManagement;
import jakarta.persistence.EntityManager;
import javafx.eventlisteners.EventListeners;
import javafx.panes.StockMarketPane;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Random;

public class StockMarketController extends CustomController {

    private static final Random rand = new Random();
    private static Tendency tendency;
    private final StockMarketPane pane;
    private final ShareDao shareDao;
    private Share share;

    enum Tendency {
        UP,
        DOWN;

        public static Tendency getRandTendency() {
            if (rand.nextBoolean()) {
                return Tendency.UP;
            }
            return Tendency.DOWN;
        }
    }


    public StockMarketController(Stage stage, StockMarketPane pane, EventListeners eventListeners, Share share) {
        super(stage, eventListeners);
        this.pane = pane;
        EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
        this.shareDao = new ShareDaoImpl(entityManager);
        this.share = share;
        tendency = Tendency.getRandTendency();
    }

    public Share share() {
        return shareDao.get(share.getId());
    }

    public void generateSampleData() {
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = startDate.plusMonths(8);
        HashMap<LocalDate, Double> values = createValueList(startDate, endDate);
        writetoCSV(values);
    }

    private HashMap<LocalDate, Double> createValueList(LocalDate startDate, LocalDate endDate) {
        HashMap<LocalDate, Double> values = new HashMap<>();
        double oldValue = rand.nextDouble(200, 1000);
        int addDayOrigin = 4;
        int addDayBound = 10;
        int additionalDays = rand.nextInt(addDayOrigin, addDayBound);
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(additionalDays)) {
            switchTendency();
            for (int i = 0; i < additionalDays; i++) {
                double newValue = getCalculatedVal(oldValue, tendency);
                values.put(date, newValue);
                oldValue = newValue;
                date.plusDays(2);
            }
            additionalDays = rand.nextInt(addDayOrigin, addDayBound);
        }
        return values;
    }

    private void switchTendency() {
        tendency = Tendency.getRandTendency();
    }


    private double getCalculatedVal(double oldValue, Tendency tendency) {
        double minVal;
        double maxVal;
        if (tendency == Tendency.UP) {
            minVal = oldValue;
            maxVal = oldValue + (oldValue * getRandomChangeRate());
        } else {
            minVal = oldValue - (oldValue * getRandomChangeRate());
            maxVal = oldValue;
        }
        return rand.nextDouble(minVal, maxVal);
    }

    private double getRandomChangeRate() {
        return rand.nextDouble(getChangeRateOrigin(), getChangeRateBound());
    }

    private double getChangeRateOrigin() {
        return rand.nextDouble(0.00045, 0.005);
    }

    private double getChangeRateBound() {
        return rand.nextDouble(0.005, 0.05);
    }


    private static void writetoCSV(HashMap<LocalDate, Double> values) {
        String filePath = System.getProperty("user.dir") + "/src/main/resources/assets/mocks/price_over_time.csv";
        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("date,price\n");
            writeValues(values, writer);
            System.out.println("CSV file created successfully at " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to CSV file.");
            e.printStackTrace();
        }
    }

    private static void writeValues(HashMap<LocalDate, Double> values, FileWriter writer) {
        values.forEach((date, value) -> {
            try {
                writer.append(date.toString()).append(",").append(String.format("%.2f", value)).append("\n");
            } catch (IOException e) {
                System.err.println("Error while writing to CSV file.");
                e.printStackTrace();
            }
        });
    }
}
