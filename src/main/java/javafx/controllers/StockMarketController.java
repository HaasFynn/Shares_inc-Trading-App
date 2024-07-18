package javafx.controllers;

import console.dao.*;
import console.entities.Share;
import console.functional.EntityManagement;
import jakarta.persistence.EntityManager;
import javafx.assets.StockEntry;
import javafx.eventlisteners.EventListeners;
import javafx.panes.StockMarketPane;
import javafx.stage.Stage;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
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

        /**
         * Gets random tendency.
         *
         * @return random tendency
         */
        public static Tendency getRandTendency() {
            if (rand.nextBoolean()) {
                return Tendency.UP;
            }
            return Tendency.DOWN;
        }
    }


    /**
     * Instantiates a new Stock market controller.
     *
     * @param stage          JavaFX stage
     * @param pane           JavaFX pane
     * @param eventListeners Interface with certain CallBacks
     * @param share          Share, which price should be represented
     */
    public StockMarketController(Stage stage, StockMarketPane pane, EventListeners eventListeners, Share share) {
        super(stage, eventListeners);
        this.pane = pane;
        EntityManager entityManager = EntityManagement.createEntityManagerFactory().createEntityManager();
        this.shareDao = new ShareDaoImpl(entityManager);
        this.share = share;
        tendency = Tendency.getRandTendency();
    }

    /**
     * Gets the newest Version of the Share
     *
     * @return Share out of Database
     */
    public Share share() {
        return shareDao.get(share.getId());
    }

    /**
     * Generate sample data.
     */
    public void generateSampleData() {
        LocalDate startDate = LocalDate.of(2025, 1, 1);
        LocalDate endDate = startDate.plusMonths(6);
        ArrayList<StockEntry> values = createValueList(startDate, endDate);
        writetoCSV(values);
    }

    private ArrayList<StockEntry> createValueList(LocalDate startDate, LocalDate endDate) {
        ArrayList<StockEntry> values = new ArrayList<>();
        double oldValue = rand.nextDouble(200, 1000);
        for (LocalDate date = startDate; date.isBefore(endDate); date = date.plusDays(1)) {
            double newValue = getNextValue(oldValue);
            values.add(new StockEntry(date, newValue));
            oldValue = newValue;
        }
        return values;
    }

    private static double getNextValue(double oldValue) {
        return oldValue + (oldValue * rand.nextDouble(-0.05, 0.05));
    }

    private static void writetoCSV(ArrayList<StockEntry> values) {
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

    private static void writeValues(ArrayList<StockEntry> values, FileWriter writer) {
        values.forEach((entry) -> {
            try {
                LocalDate date = entry.date();
                double value = entry.price();
                writer.append(date.toString()).append(",").append(String.format("%.2f", value)).append("\n");
            } catch (IOException e) {
                System.err.println("Error while writing to CSV file.");
                e.printStackTrace();
            }
        });
    }
}
