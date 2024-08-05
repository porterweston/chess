package dataaccess.mysql;

import dataaccess.*;
import dataaccess.interfaces.*;
import model.*;
import org.mindrot.jbcrypt.BCrypt;

import java.sql.*;

public class MySQLUserDAO extends MySQLDAO implements UserDAO {

    public MySQLUserDAO() throws DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS users (
                username VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL,
                PRIMARY KEY (username)
            );
            """
        };
        configureDatabase(createStatements);
    }

    public UserData getUser(String username) {
        try (var conn = DatabaseManager.getConnection()) {
            String preparedStatement = "SELECT username, password, email FROM users WHERE username=?;";
            try (var ps = conn.prepareStatement(preparedStatement)) {
                ps.setString(1, username);
                ResultSet rs = ps.executeQuery();
                rs.next();
                return new UserData(rs.getString("username"),
                        rs.getString("password"), rs.getString("email"));
            }
        } catch (DataAccessException | SQLException e) {
            return null;
        }
    }

    public void createUser(UserData userData) throws DataAccessException {
        String preparedStatement = "INSERT INTO users (username, password, email) VALUES (?, ?, ?);";
        String hashedPassword = BCrypt.hashpw(userData.password(), BCrypt.gensalt());
        executeUpdate(preparedStatement, userData.username(), hashedPassword, userData.email());
    }

    public void deleteUsers() {
        String preparedStatement = "TRUNCATE users;";
        try {
            executeUpdate(preparedStatement);
        } catch (DataAccessException e) {
            return;
        }
    }
}
