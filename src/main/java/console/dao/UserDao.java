package console.dao;

import console.entities.User;

import java.util.List;

/**
 * The interface User dao.
 */
public interface UserDao {
    /**
     * Get user.
     *
     * @param id the id
     * @return the user
     */
    User get(long id);

    /**
     * Gets by username.
     *
     * @param username the username
     * @return the by username
     */
    User getByUsername(String username);

    /**
     * Gets by password.
     *
     * @param username the username
     * @param password the password
     * @return the by password
     */
    User getByPassword(String username, String password);

    /**
     * Gets all.
     *
     * @return the all
     */
    List<User> getAll();

    /**
     * Add boolean.
     *
     * @param user the user
     * @return the boolean
     */
    boolean add(User user);

    /**
     * Update boolean.
     *
     * @param user the user
     * @return the boolean
     */
    boolean update(User user);

    /**
     * Delete boolean.
     *
     * @param user the user
     * @return the boolean
     */
    boolean delete(User user);
}
