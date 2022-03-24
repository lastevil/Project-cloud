package gb.project.cloud.server.auth;

import lombok.extern.slf4j.Slf4j;

import java.sql.SQLException;

@Slf4j
public class AuthService implements Authentication {
    private DBConnect DB;

    public AuthService() {
        try {
            DB =new DBConnect();
            DB.setConnection();
            DB.createTableOfUsers();
            DB.disconnect();
        } catch (SQLException e) {
            log.error("SQL ERROR");
            e.printStackTrace();
        } finally {
            DBConnect.disconnect();
        }
    }

    public void close(){
        DB.disconnect();
    }

    public void open(){
        try {
            DB.setConnection();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean existUserByLoginPass(String login, String password) throws SQLException {
            return DB.userExistByUserdata(login, password);
    }

    @Override
    public boolean registration(String login, String password) {
        return DB.userAdd(login, password);
    }

}
