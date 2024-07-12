package console.dao;

import console.entities.Portfolio;

import java.util.List;

/**
 * The interface Portfolio dao.
 */
public interface PortfolioDao {
    /**
     * Get portfolio.
     *
     * @param id the id
     * @return the portfolio
     */
    Portfolio get(long id);

    /**
     * Get portfolio.
     *
     * @param shareId the share id
     * @param userId  the user id
     * @return the portfolio
     */
    Portfolio get(long shareId, long userId);

    /**
     * Gets all.
     *
     * @return the all
     */
    List<Portfolio> getAll();

    /**
     * Gets user portfolio.
     *
     * @param userId the user id
     * @return the user portfolio
     */
    List<Portfolio> getUserPortfolio(long userId);

    /**
     * Add boolean.
     *
     * @param portfolio the portfolio
     * @return the boolean
     */
    boolean add(Portfolio portfolio);

    /**
     * Update boolean.
     *
     * @param portfolio the portfolio
     * @return the boolean
     */
    boolean update(Portfolio portfolio);

    /**
     * Delete boolean.
     *
     * @param portfolio the portfolio
     * @return the boolean
     */
    boolean delete(Portfolio portfolio);
}
