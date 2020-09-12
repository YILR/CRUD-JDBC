package crud_jdbc;

import java.util.List;

public interface UserDao {
    List<User> findAll();
    User findById(int id);
    void insert(User user);
    void update(User user);
    void delete(int id);
    void create();
    void drop();
}
