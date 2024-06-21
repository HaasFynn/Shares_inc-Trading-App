package console.dao;

import console.entities.Share;

import java.util.List;

public interface ShareDao {
    Share get(long id);

    Share getByName(String name);

    List<Share> getAll();

    List<Share> getByNamePrompt(String prompt);

    boolean add(Share share);

    boolean addAll(Share... share);

    boolean update(Share share);

    boolean delete(Share share);
}
