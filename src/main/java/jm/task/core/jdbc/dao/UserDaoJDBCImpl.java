package jm.task.core.jdbc.dao;

import jm.task.core.jdbc.model.User;
import jm.task.core.jdbc.util.Util;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoJDBCImpl implements UserDao {
    private static final String CREATE_TABLE_USERS = "CREATE TABLE IF NOT EXISTS Users" +
            "(id bigint primary key auto_increment, name varchar(50), lastName varchar(50), age tinyint)";
    private static final String DROP_TABLE = "DROP TABLE IF EXISTS Users";
    private static final String INSERT_USER = "INSERT INTO Users (name, lastName, age) VALUES (?, ?, ?)";
    private static final String DELETE_USER = "DELETE FROM Users WHERE id = ?";
    private static final String SELECT_ALL_USERS = "SELECT * FROM Users";
    private static final String CLEAN_USERS = "DELETE FROM Users";
    private Connection connection = Util.getConnection();

    public void createUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.execute(CREATE_TABLE_USERS);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void dropUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(DROP_TABLE);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void saveUser(String name, String lastName, byte age) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(INSERT_USER)) {
            preparedStatement.setString(1, name);
            preparedStatement.setString(2, lastName);
            preparedStatement.setByte(3, age);
            preparedStatement.executeUpdate();
            System.out.printf("Пользователь с именем %s добавлен в базу данных\n", name);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void removeUserById(long id) {
        try (PreparedStatement preparedStatement = connection.prepareStatement(DELETE_USER)) {
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
            ResultSet rs = statement.executeQuery(SELECT_ALL_USERS);
            while (rs.next()) {
                long id = rs.getLong("id");
                String name = rs.getString("name");
                String lastName = rs.getString("lastName");
                byte age = rs.getByte("age");
                User user = new User(name, lastName, age);
                user.setId(id);
                list.add(user);
            }
            return list;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void cleanUsersTable() {
        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(CLEAN_USERS);
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}