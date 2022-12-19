package jm.task.core.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Util {
    // реализуйте настройку соеденения с БД
    private static final String URL = "jdbc:mysql://localhost:3306/test_schema";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "root";
    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                connection.setAutoCommit(false);
                System.out.println("Установлено соединение с БД");
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Не удалось подключиться к БД");
            }
        }
        return connection;
    }
}
