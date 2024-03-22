package dao;

import entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.transaction.Transaction;
import jakarta.transaction.Transactional;

import java.util.List;

public class UserDao implements Dao<User> {
    private final EntityManager entityManager;
    private final EntityTransaction transaction;

    public UserDao(EntityManager entityManager) {
        this.entityManager = entityManager;
        this.transaction = this.entityManager.getTransaction();
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

    //TODO Transactional fixxen

    @Override
    public void add(User user) {
        begin();
        entityManager.persist(user);
        commit();
    }

    @Transactional
    @Override
    public void update(User user) {
        begin();
        entityManager.merge(user);
        commit();
    }


    @Override
    public void delete(User user) {
        begin();
        entityManager.remove(user);
        commit();
    }

    private void begin() {
        entityManager.getTransaction().begin();
    }

    private void commit() {
        entityManager.getTransaction().commit();
    }
}
