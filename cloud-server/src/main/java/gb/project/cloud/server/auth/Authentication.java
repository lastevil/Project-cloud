package gb.project.cloud.server.auth;

import java.sql.SQLException;

public interface Authentication {
    boolean existUserByLoginPass(String login, String password) throws SQLException;
    boolean registration(String login, String password);
}