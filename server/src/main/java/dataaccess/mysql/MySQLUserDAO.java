package dataaccess.mysql;

import dataaccess.*;
import dataaccess.interfaces.*;
import model.*;

import java.sql.SQLException;

public class MySQLUserDAO implements UserDAO{
    public UserData getUser(String username) {
        return null;
    }

    public void createUser(UserData userData) throws DataAccessException {

    }

    public void deleteUsers() {

    }

    private final String[] createStatements = {
            """
            CREATE TABLE IF NOT EXISTS users (
                username VARCHAR(255) NOT NULL,
                password VARCHAR(255) NOT NULL,
                email VARCHAR(255) NOT NULL,
                PRIMARY KEY (username)
            );
            """
    };

    private void configureDatabase() throws DataAccessException{
        DatabaseManager.createDatabase();
        try (var conn = DatabaseManager.getConnection()) {
            for (var statement : createStatements) {
                try (var preparedStatement = conn.prepareStatement(statement)) {
                    preparedStatement.executeUpdate();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException("Unable to configure database");
        }
    }
}
