package console.dao;

import console.entities.Tag;

import java.util.List;

public interface TagDao {

    Tag get(long id);

    Tag getByName(String name);

    List<Tag> getAll();

    boolean add(Tag tag);

    boolean addAll(Tag... tags);

    boolean update(Tag tag);

    boolean delete(Tag tag);
}
