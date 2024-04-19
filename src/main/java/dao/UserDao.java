package dao;

import entities.User;

import java.util.List;

public interface UserDao {
    User get(long id);

    User getByUsername(String username);

    User getByPassword(String username, String password);

    List<User> getAll();

    boolean add(User user);

    boolean update(User user);

    boolean delete(User user);
}
