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
        String hashPass = String.valueOf(password.hashCode());
        String hashLogin = String.valueOf(login.hashCode());
            return DB.userExistByUserdata(hashLogin, hashPass);
    }

    @Override
    public boolean registration(String login, String password) {
        String hashPass = String.valueOf(password.hashCode());
        String hashLogin = String.valueOf(login.hashCode());
        return DB.userAdd(hashLogin, hashPass);
    }

}
