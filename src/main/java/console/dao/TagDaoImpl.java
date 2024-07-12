package console.dao;


import console.entities.Tag;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityNotFoundException;
import jakarta.persistence.NoResultException;

import java.util.List;

public class TagDaoImpl implements TagDao {
    private final EntityManager entityManager;

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
            entityManager.getTransaction().rollback();
            System.err.println("No tag found with name " + name + "\n" + e);
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
            return true;
        } catch (EntityExistsException e) {
            entityManager.getTransaction().rollback();
            System.err.println("Error while adding tag, entity already exists... " + e);
            return false;
        }
    }

    @Override
    public boolean addAll(Tag... tags) {
        for (Tag tag : tags) {
            try {
                entityManager.getTransaction().begin();
                entityManager.persist(tag);
                entityManager.getTransaction().commit();
            } catch (EntityExistsException e) {
                entityManager.getTransaction().rollback();
                System.err.println("Error while adding tag, entity already exists... " + e);
                return false;
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
            return true;
        } catch (IllegalArgumentException e) {
            entityManager.getTransaction().rollback();
            System.err.println("Error while updating tag, entity could not be found... " + e);
            return false;
        }
    }

    @Override
    public boolean delete(Tag tag) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(tag);
            entityManager.getTransaction().commit();
            return true;
        } catch (EntityNotFoundException e) {
            entityManager.getTransaction().rollback();
            System.err.println("Error while deleting tag, entity does not exist... " + e);
            return false;
        }
    }
}
