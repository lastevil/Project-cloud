package gb.project.cloud.server.auth;

import lombok.extern.slf4j.Slf4j;

import java.sql.*;

@Slf4j
public class DBConnect {
    private static Connection connection;
    private static Statement statement;

    public static void setConnection() throws SQLException {
        connection = DriverManager.getConnection("jdbc:sqlite:javadb.db");
        statement = connection.createStatement();
        connection.setAutoCommit(true);
    }

    public static void disconnect() {
        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            log.error("SQL ERROR");
        }
    }

    public static void createTableOfUsers() throws SQLException {
        statement.executeUpdate("" +
                "CREATE TABLE IF not exists USERS" +
                "(ID integer not null primary key autoincrement," +
                "login text not null unique," +
                "password text not null" +
                ");"
        );
    }

    public static boolean userAdd(String login, String password) {
        try (final PreparedStatement ps = connection
                .prepareStatement("INSERT INTO USERS " +
                        "(login,password) VALUES (?,?);"
                )) {
            ps.setString(1, login);
            ps.setString(2, password);
            ps.executeUpdate();
            return true;
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            log.error("SQL ERROR");
            return false;
        }
    }

    public static boolean userExistByUserdata(String login, String password) throws SQLException {
        try (PreparedStatement ps = connection.prepareStatement(
                "SELECT ID FROM USERS" +
                        " WHERE login = ? and password = ?;"
        )) {
            ps.setString(1, login);
            ps.setString(2, password);
            ResultSet rs = ps.executeQuery();
            boolean user = false;
            while (rs.next()) {
                if (!rs.getString(1).equals("")) {
                    user = true;
                }
            }
            return user;
        }
    }
}
