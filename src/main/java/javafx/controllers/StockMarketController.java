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
import java.util.Random;

public class StockMarketController extends CustomController {

    private final StockMarketPane pane;
    private final ShareDao shareDao;
    private Share share;

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
        String filePath = System.getProperty("user.dir") + "/src/main/resources/assets/mocks/price_over_time.csv";

        LocalDate startDate = LocalDate.of(2023, 7, 1);
        LocalDate endDate = startDate.plusYears(1);

        double minPrice = 50.0;
        double maxPrice = 200.0;

        Random random = new Random();

        try (FileWriter writer = new FileWriter(filePath)) {
            writer.append("date,price\n");

            for (LocalDate date = startDate; !date.isAfter(endDate); date = date.plusDays(1)) {
                double price = minPrice + (maxPrice - minPrice) * random.nextDouble();
                writer.append(date.toString()).append(",").append(String.format("%.2f", price)).append("\n");
            }

            System.out.println("CSV file created successfully at " + filePath);
        } catch (IOException e) {
            System.err.println("Error writing to CSV file.");
            e.printStackTrace();
        }
    }
}
