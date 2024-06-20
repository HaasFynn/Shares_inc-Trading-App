package console.dao;

import console.entities.Portfolio;

import java.util.List;

public interface PortfolioDao {
    Portfolio get(long id);

    Portfolio get(long shareId, long userId);

    List<Portfolio> getAll();

    List<Portfolio> getAllFromUser(long userId);

    boolean add(Portfolio portfolio);

    boolean update(Portfolio portfolio);

    boolean delete(Portfolio portfolio);
}
