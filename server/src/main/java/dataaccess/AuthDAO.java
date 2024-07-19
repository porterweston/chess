package dataaccess;

import model.*;

public interface AuthDAO {
    public AuthData getAuth(String authToken) throws DataAccessException;
    public String createAuth(String username) throws DataAccessException;
    public void deleteAuth(String authToken) throws DataAccessException;
    public void deleteAuths();
}
