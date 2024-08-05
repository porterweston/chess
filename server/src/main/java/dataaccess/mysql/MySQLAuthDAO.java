package dataaccess.mysql;

import dataaccess.*;
import dataaccess.interfaces.*;
import model.*;

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
        return null;
    }

    public String createAuth(String username) {
        return null;
    }

    public void deleteAuth(String authToken) throws DataAccessException {

    }

    public void deleteAuths() {

    }
}
