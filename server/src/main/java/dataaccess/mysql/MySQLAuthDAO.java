package dataaccess.mysql;

import dataaccess.*;
import dataaccess.interfaces.*;
import model.*;

public class MySQLAuthDAO implements AuthDAO{
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
