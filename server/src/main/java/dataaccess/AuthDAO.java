package dataaccess;

import model.*;

public interface AuthDAO {
    public AuthData getAuth(String authToken) throws DataAccessException;
    public String createAuth(String username);
    public void deleteAuth(String authToken) throws DataAccessException;
    public void deleteAuths();
}
