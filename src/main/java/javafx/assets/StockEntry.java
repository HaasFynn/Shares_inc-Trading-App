package javafx.assets;

import java.time.LocalDate;

/**
 * @param date saves a LocalDate price
 * @param price saves a double price
 */
public record StockEntry(LocalDate date, double price) {
}