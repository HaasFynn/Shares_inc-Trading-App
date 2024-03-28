package dao;

import entities.User;
import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.TransactionRequiredException;
import jakarta.transaction.Transactional;

import java.util.List;

public class UserDao implements Dao<User> {
    private final EntityManager entityManager;

    public UserDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User get(long id) {
        return entityManager.find(User.class, id);
    }

    public User getByUsername(String username) {
        return entityManager.createQuery("FROM User u WHERE u.username = :username", User.class).
                setParameter("username", username).
                getResultStream().findFirst().orElse(null);
    }

    public User getByPassword(String username, String password) {
        return entityManager.createQuery("FROM User u WHERE u.username = :username AND u.password = :password", User.class)
                .setParameter("username", username)
                .setParameter("password", password)
                .getSingleResult();
    }

    @Override
    public List<User> getAll() {
        return entityManager.createQuery("FROM User", User.class)
                .getResultList();
    }

    @Override
    public boolean add(User user) {
        try {
            entityManager.getTransaction().begin();
            entityManager.persist(user);
            entityManager.getTransaction().commit();
        } catch (EntityExistsException e) {
            return false;
        }
        return true;
    }

    @Transactional
    @Override
    public boolean update(User user) {
        try {
            entityManager.getTransaction().begin();
            entityManager.merge(user);
            entityManager.getTransaction().commit();
        } catch (TransactionRequiredException e) {
            return false;
        }
        return true;
    }


    @Override
    public boolean delete(User user) {
        try {
            entityManager.getTransaction().begin();
            entityManager.remove(user);
            entityManager.getTransaction().commit();
        } catch (TransactionRequiredException e) {
            return false;
        }
        return true;
    }

}
