package console.dao;

import console.entities.Picture;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;

import java.util.List;

/**
 * The type Picture dao.
 */
public class PictureDaoImpl implements PictureDao {
    private final EntityManager entityManager;

    /**
     * Instantiates a new Picture dao.
     *
     * @param entityManager the entity manager
     */
    public PictureDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Picture get(int id) {
        return entityManager.find(Picture.class, id);
    }

    @Override
public Picture getByUserId(long userId) {
    try {
        return entityManager.createQuery("from Picture as p where p.userIDFK = :user_idfk", Picture.class)
                .setParameter("user_idfk", userId)
                .getSingleResult();
    } catch (NoResultException e) {
        entityManager.getTransaction().rollback();
        System.err.println("Picture not found for User with userId: " + userId);
        return null;
    }
}

    @Override
    public List<Picture> getAll() {
        return entityManager.createQuery("from Picture", Picture.class).getResultList();
    }

    @Override
    public boolean add(Picture picture) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(picture);
            entityManager.getTransaction().commit();
            return true;
        } catch (EntityExistsException e) {
            entityManager.getTransaction().rollback();
            System.err.println("Error while adding picture, entity already exists..." + e.getMessage());
            return false;
        }
    }

    @Override
    public boolean update(Picture picture) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(picture);
            entityManager.getTransaction().commit();
            return true;
        } catch (Exception ignored) {
            return false;
        }
    }

    @Override
    public boolean delete(Picture picture) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(picture);
            entityManager.getTransaction().commit();
            return true;
        } catch (EntityNotFoundException e) {
            entityManager.getTransaction().rollback();
            System.err.println("Error while deleting picture, entity does not exist..." + e.getMessage());
            return false;
        }
    }

}
