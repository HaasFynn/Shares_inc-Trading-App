package console.dao;

import console.entities.Share;

import java.util.List;

/**
 * The interface Share dao.
 */
public interface ShareDao {
    /**
     * Get share.
     *
     * @param id the id
     * @return the share
     */
    Share get(long id);

    /**
     * Gets by name.
     *
     * @param name the name
     * @return the by name
     */
    Share getByName(String name);

    /**
     * Gets all.
     *
     * @return the all
     */
    List<Share> getAll();

    /**
     * Gets by name prompt.
     *
     * @param prompt the prompt
     * @return the by name prompt
     */
    List<Share> getByNamePrompt(String prompt);

    /**
     * Add boolean.
     *
     * @param share the share
     * @return the boolean
     */
    boolean add(Share share);

    /**
     * Add all boolean.
     *
     * @param share the share
     * @return the boolean
     */
    boolean addAll(Share... share);

    /**
     * Update boolean.
     *
     * @param share the share
     * @return the boolean
     */
    boolean update(Share share);

    /**
     * Delete boolean.
     *
     * @param share the share
     * @return the boolean
     */
    boolean delete(Share share);
}
