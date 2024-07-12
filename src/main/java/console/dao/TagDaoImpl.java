package console.dao;


import console.entities.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;

import java.util.List;

/**
 * The type Tag dao.
 */
public class TagDaoImpl implements TagDao {
    private final EntityManager entityManager;

    /**
     * Instantiates a new Tag dao.
     *
     * @param entityManager the entity manager
     */
    public TagDaoImpl(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Tag get(long id) {
        try {
            return entityManager.find(Tag.class, id);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public Tag getByName(String name) {
        try {
            return entityManager.createQuery("FROM Tag t WHERE t.name = :name", Tag.class)
                    .setParameter("name", name)
                    .getResultStream()
                    .findFirst().orElse(null);
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public List<Tag> getAll() {
        return entityManager.createQuery("FROM Tag", Tag.class).getResultList();
    }

    @Override
    public boolean add(Tag tag) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(tag);
            entityManager.getTransaction().commit();
        } catch (EntityExistsException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean addAll(Tag... tags) {
        for (Tag tag : tags) {
            try {
                entityManager.getTransaction().begin();
                entityManager.persist(tag);
                entityManager.getTransaction().commit();
            } catch (EntityExistsException ignored) {
            }
        }
        return true;
    }

    @Override
    public boolean update(Tag tag) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(tag);
            entityManager.getTransaction().commit();
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }

    @Override
    public boolean delete(Tag tag) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(tag);
            entityManager.getTransaction().commit();
        } catch (IllegalArgumentException e) {
            return false;
        }
        return true;
    }
}
