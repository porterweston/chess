package dataaccess.mysql;

import dataaccess.*;
import dataaccess.interfaces.*;
import model.*;

import java.sql.SQLException;

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
        return null;
    }

    public void createUser(UserData userData) throws DataAccessException {

    }

    public void deleteUsers() {

    }
}
