package dao;

import java.util.List;

public interface Dao<T> {
    T get(long id);

    List<T> getAll();

    boolean add(T t);

    boolean update(T t);

    boolean delete(T t);
}
