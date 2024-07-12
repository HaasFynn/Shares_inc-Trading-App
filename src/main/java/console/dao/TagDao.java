package console.dao;

import console.entities.Tag;

import java.util.List;

/**
 * The interface Tag dao.
 */
public interface TagDao {

    /**
     * Get tag.
     *
     * @param id the id
     * @return the tag
     */
    Tag get(long id);

    /**
     * Gets by name.
     *
     * @param name the name
     * @return the by name
     */
    Tag getByName(String name);

    /**
     * Gets all.
     *
     * @return the all
     */
    List<Tag> getAll();

    /**
     * Add boolean.
     *
     * @param tag the tag
     * @return the boolean
     */
    boolean add(Tag tag);

    /**
     * Add all boolean.
     *
     * @param tags the tags
     * @return the boolean
     */
    boolean addAll(Tag... tags);

    /**
     * Update boolean.
     *
     * @param tag the tag
     * @return the boolean
     */
    boolean update(Tag tag);

    /**
     * Delete boolean.
     *
     * @param tag the tag
     * @return the boolean
     */
    boolean delete(Tag tag);
}
