package console.dao;

import console.entities.Picture;
import jakarta.persistence.EntityManager;

import java.util.List;

public class PictureDaoImpl implements PictureDao{

    private final EntityManager entityManager;

    public PictureDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Picture get(int id) {
        return null;
    }

    /**
     * @return
     */
    @Override
    public List<Picture> getAll() {
        return List.of();
    }

    /**
     * @param picture
     * @return
     */
    @Override
    public Picture save(Picture picture) {
        return null;
    }

    @Override
    public boolean delete(Picture picture) {
        return false;
    }

    @Override
    public boolean update(Picture picture) {
        return false;
    }
}
