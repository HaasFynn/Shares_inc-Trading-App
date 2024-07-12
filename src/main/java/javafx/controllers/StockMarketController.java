package javafx.controllers;

import console.dao.*;
import console.entities.Share;
import console.functional.EntityManagement;
import jakarta.persistence.EntityManager;
import javafx.eventlisteners.EventListeners;
import javafx.pages.StockMarketPane;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.Random;

public class StockMarketController extends CustomController {

    private static final Random rand = new Random();

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
        int i = 0;
        Tendency tendency = Tendency.getRandTendency();
        for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(4)) {
            tendency = switchTendency(tendency, i);
            double newValue = getCalculatedVal(oldValue, tendency);
            values.put(date, newValue);
            oldValue = newValue;
            i++;
        }
        return values;
    }

    private Tendency switchTendency(Tendency tendency, int i) {
        if (i % rand.nextInt(2, 9) == 0) {
            return Tendency.getRandTendency();
        }
        return tendency;
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
        return rand.nextDouble(getRandChangeRateOrigin(), getRandChangeRateBound());
    }

    private static double getRandChangeRateBound() {
        return rand.nextDouble(0.009, 0.1);
    }

    private static double getRandChangeRateOrigin() {
        return rand.nextDouble(0.0009, 0.01);
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
