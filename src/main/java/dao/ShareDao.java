package dao;

import entities.Share;

import java.util.List;

public interface ShareDao {
    Share get(long id);

    Share getByName(String name);

    List<Share> getAll();

    boolean add(Share t);

    boolean update(Share t);

    boolean delete(Share t);
}
