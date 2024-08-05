package dataaccess.mysql;

import dataaccess.*;
import dataaccess.interfaces.*;
import model.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class MySQLAuthDAO extends MySQLDAO implements AuthDAO{
    public MySQLAuthDAO() throws DataAccessException {
        String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS auths(
                authToken VARCHAR(255) NOT NULL,
                username VARCHAR(255) NOT NULL,
                PRIMARY KEY (authToken)
            );
            """
        };
        configureDatabase(createStatements);
    }

    public AuthData getAuth(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            String preparedStatement = "SELECT authToken, username FROM auths WHERE authToken=?;";
            try (var ps = conn.prepareStatement(preparedStatement)) {
                ps.setString(1, authToken);
                ResultSet rs = ps.executeQuery();
                rs.next();
                return new AuthData(rs.getString("authToken"),
                        rs.getString("username"));
            }
        } catch (DataAccessException | SQLException e) {
            throw new DataAccessException("Authentication does not exist");
        }
    }

    public String createAuth(String username) {
        String preparedStatement = "INSERT INTO auths (authToken, username) VALUES (?, ?);";
        String authToken = UUID.randomUUID().toString();
        try {
            executeUpdate(preparedStatement, authToken, username);
            return authToken;
        } catch (DataAccessException e) {
            return null;
        }
    }

    public void deleteAuth(String authToken) throws DataAccessException {
        String preparedStatement = "DELETE FROM auths WHERE authToken=?;";
        executeUpdate(preparedStatement, authToken);
    }

    public void deleteAuths() {
        String preparedStatement = "TRUNCATE auths;";
        try {
            executeUpdate(preparedStatement);
        } catch (DataAccessException e) {
            return;
        }
    }

    public Collection<AuthData> getAuthsDatabase() {
        HashSet<AuthData> auths = new HashSet<>();
        try (var conn = DatabaseManager.getConnection()) {
            String preparedStatement = "SELECT * FROM auths";
            try (var ps = conn.prepareStatement(preparedStatement)) {
                ResultSet rs = ps.executeQuery();
                while(rs.next()) {
                    AuthData curAuth = new AuthData(rs.getString("authToken"),
                            rs.getString("username"));
                    auths.add(curAuth);
                }
            }
            return auths;
        } catch (DataAccessException | SQLException e) {
            return null;
        }
    }
}
