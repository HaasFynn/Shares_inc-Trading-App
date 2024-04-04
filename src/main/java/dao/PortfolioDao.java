package dao;

import entities.Portfolio;

import java.util.List;

public interface PortfolioDao {
    Portfolio get(long id);

    Portfolio get(long shareId, long userId);

    List<Portfolio> getAll();

    boolean add(Portfolio t);

    boolean update(Portfolio t);

    boolean delete(Portfolio t);
}
