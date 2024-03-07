package dataAccess;

import model.AuthData;

public interface AuthDAO {
    public AuthData getAuth(String authToken) throws DataAccessException;
    public void createAuth(String username)throws DataAccessException;
    public void deleteAuth(String authToken)throws DataAccessException;
    public void clearAuths()throws DataAccessException;
}
