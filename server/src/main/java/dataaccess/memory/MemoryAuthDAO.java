package dataaccess.memory;

import dataaccess.interfaces.AuthDAO;
import dataaccess.DataAccessException;
import model.AuthData;

import java.util.Collection;
import java.util.HashSet;
import java.util.UUID;

public class MemoryAuthDAO implements AuthDAO {

    private static Collection<AuthData> auths = new HashSet<AuthData>();

    @Override
    public AuthData getAuth(String authToken) throws DataAccessException {
        for (AuthData auth : auths) {
            if (auth.authToken().equals(authToken)) {
                return auth;
            }
        }
        throw new DataAccessException("Authentication does not exist");
    }

    @Override
    public String createAuth(String username) {
        AuthData newAuth = new AuthData(UUID.randomUUID().toString(), username);
        auths.add(newAuth);
        return newAuth.authToken();
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        for (AuthData auth : auths) {
            if (auth.authToken().equals(authToken)) {
                auths.remove(auth);
                return;
            }
        }
        throw new DataAccessException("Authentication does not exist");
    }

    @Override
    public void deleteAuths() {
        auths.clear();
    }

    //returns what's in the database for testing purposes
    public Collection<AuthData> getAuthsDatabase() {
        return auths;
    }
}
