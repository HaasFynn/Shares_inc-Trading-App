package dao;

import entities.Share;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.transaction.Transactional;

import java.util.List;

public class ShareDao implements Dao<Share> {
    private final EntityManager entityManager;

    public ShareDao(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public Share get(long id) {
        return entityManager.find(Share.class, id);
    }

    public Share getByName(String name) {
        return entityManager.createQuery("FROM Share s WHERE s.name = :name", Share.class)
                .setParameter("name", name)
                .getResultStream().findFirst().orElse(null);
    }

    @Override
    public List<Share> getAll() {
        return entityManager.createQuery("FROM Share", Share.class)
                .getResultList();
    }


    @Override
    public void add(Share share) {
        begin();
        entityManager.persist(share);
        commit();
    }

    @Override
    public void update(Share share) {
        begin();
        entityManager.merge(share);
        commit();
    }


    @Override
    public void delete(Share share) {
        begin();
        entityManager.remove(share);
        commit();
    }

    private void begin() {
        entityManager.getTransaction().begin();
    }

    private void commit() {
        entityManager.getTransaction().commit();
    }
}
