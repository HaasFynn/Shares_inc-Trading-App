package console.dao;

import console.entities.Picture;

import java.util.List;

/**
 * The interface Picture dao.
 */
public interface PictureDao {

    /**
     * Get picture.
     *
     * @param id the id
     * @return the picture
     */
    Picture get(int id);

    /**
     * Gets by user id.
     *
     * @param userId the user id
     * @return the by user id
     */
    Picture getByUserId(long userId);

    /**
     * Gets all.
     *
     * @return the all
     */
    List<Picture> getAll();

    /**
     * Save picture.
     *
     * @param picture the picture
     * @return the picture
     */
    boolean add(Picture picture);

    /**
     * Update boolean.
     *
     * @param picture the picture
     * @return the boolean
     */
    boolean update(Picture picture);

    /**
     * Delete boolean.
     *
     * @param picture the picture
     * @return the boolean
     */
    boolean delete(Picture picture);

    /**
     * Gets by name.
     *
     * @param name the name
     * @return the by name
     */
    Picture getByName(String name);
}
