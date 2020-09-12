package crud_jdbc;

import java.util.ArrayList;
import java.util.List;

public class App {

    private static List<User> users = new ArrayList<>();
    private static UserDao userDao = new UserDaoImpl();

    public static void main(String[] args) {

        drop();
        create();
        insert();
        users.addAll(findAll());
        update();
        findAll();

    }

    public static void create() {
        userDao.create();
    }

    public static void insert() {
        User user1 = new User("User_name1", "User_last1", 3500);
        User user2 = new User("User_name2", "User_last2", 2800);
        User user3 = new User("User_name3", "User_last3", 5000);
        User user4 = new User("User_name4", "User_last4", 4200);
        userDao.insert(user1);
        userDao.insert(user2);
        userDao.insert(user3);
        userDao.insert(user4);
    }

    public static List<User> findAll() {
        return userDao.findAll();
    }

    public static User findById(int id){
        return userDao.findById(id);
    }

    public static void update() {
        users.forEach(x->{
            x.setBalance(x.getBalance()+500);
            userDao.update(x);});
    }

    public static void delete(int id) {
        userDao.delete(id);
    }

    public static void drop() {
        userDao.drop();
    }
}
