package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static long countId;
    private static final Connection connection = Util.getConnection();

    public UserDaoJDBCImpl() {

    }

    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            String createTable = "CREATE TABLE IF NOT EXISTS Users(id bigint, name varchar(50)," +
                    " lastName varchar(50), age tinyint)";
            statement.executeUpdate(createTable);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException exc) {
                exc.printStackTrace();
            }
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            String dropTable = "DROP TABLE IF EXISTS Users";
            statement.executeUpdate(dropTable);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException exc) {
                exc.printStackTrace();
            }
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        String saveUser = "INSERT INTO Users (id, name, lastName, age) VALUES (?, ?, ?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(saveUser)) {
            preparedStatement.setLong(1, ++countId);
            preparedStatement.setString(2, name);
            preparedStatement.setString(3, lastName);
            preparedStatement.setByte(4, age);

            preparedStatement.executeUpdate();
            System.out.printf("Пользователь с именем %s добавлен в базу данных\n", name);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        String removeUser = "DELETE FROM Users WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(removeUser)) {
            preparedStatement.setLong(1, id);

            preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public List<User> getAllUsers() {
        List<User> list = new ArrayList<>();
        try (Statement statement = connection.createStatement()) {

            String getUsers = "SELECT * FROM Users";
            ResultSet rs = statement.executeQuery(getUsers);
            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String lastName = rs.getString("lastName");
                byte age = rs.getByte("age");
                User user = new User(name, lastName, age);
                user.setId(id);
                list.add(user);
            }
            //connection.commit();
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            String cleanTable = "DELETE FROM Users";
            statement.executeUpdate(cleanTable);
            connection.commit();
        } catch (SQLException e) {
            try {
                connection.rollback();
            } catch (SQLException exc) {
                exc.printStackTrace();
            }
        }
    }
}
