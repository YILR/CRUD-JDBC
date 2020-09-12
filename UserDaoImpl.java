package crud_jdbc;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private final String DATABASE_URL = "jdbc:postgresql://localhost:5432/postgres";
    private final String USER = "postgres";
    private final String PASS = "admin";

    private String CREATE = "CREATE TABLE Users (id SERIAL PRIMARY KEY, firstName VARCHAR(15), lastName VARCHAR(15), balance INT NOT NULL DEFAULT 0)";
    private String INSERT = "INSERT INTO Users(firstName, lastName, balance) VALUES(?, ?, ?)";
    private String DROP = "DROP TABLE Users";
    private String DELETE = "DELETE FROM Users WHERE id = ?";
    private String UPDATE = "UPDATE Users SET balance = ? WHERE id = ?";

    private static Savepoint savepoint;

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM Users")) {
            ResultSet rs = statement.executeQuery();
            while (rs.next()) {
                int id = rs.getInt("id");
                String first = rs.getString(2);
                String last = rs.getString(3);
                int balance = rs.getInt(4);
                System.out.println(id + " " + first + " " + last + " " + balance);
                users.add(new User(id, first, last, balance));
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return users;
    }

    @Override
    public User findById(int id) {
        try (PreparedStatement statement = getConnection().prepareStatement("SELECT * FROM Users WHERE id = ?")){
            ResultSet rs = statement.executeQuery();
            return new User(rs.getInt("id"), rs.getString(2), rs.getString(3), rs.getInt(4));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void insert(User user) {
        try (Connection con = getConnection()){
             try (PreparedStatement statement = getConnection().prepareStatement(INSERT)){

                 con.setAutoCommit(false);
                 savepoint = con.setSavepoint("save");
                 statement.setString(1, user.getFirstName());
                 statement.setString(2, user.getLastName());
                 statement.setInt(3, user.getBalance());
                 statement.executeUpdate();

                 con.commit();
                 con.setAutoCommit(true);

                 ResultSet rs = statement.getGeneratedKeys();
                 if(rs.next())
                     user.setId(rs.getInt("id"));

             }catch (SQLException e) {
                 try {
                     con.rollback(savepoint);
                 } catch (SQLException e1) {
                     e1.printStackTrace();
                 }
             }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(User user) {
        try (Connection con = getConnection();
             PreparedStatement statement = con.prepareStatement(UPDATE)) {
                statement.setInt(2, user.getId());
                statement.setInt(1, user.getBalance());
                statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void delete(int id) {
        try (PreparedStatement statement = getConnection().prepareStatement(DELETE)) {
            statement.setInt(1, id);
            statement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void create() {
        try (Statement statement = getConnection().createStatement()) {
            statement.execute(CREATE);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void drop() {
        try (Statement statement = getConnection().createStatement()) {
            statement.executeUpdate(DROP);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DATABASE_URL, USER, PASS);
    }

}
