package dataaccess;

import model.AuthData;

import java.util.Collection;
import java.util.HashSet;

public class MemoryAuthDAO implements AuthDAO{

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
    public void createAuth(AuthData authData) throws DataAccessException {
        if (auths.contains(authData)) {
            throw new DataAccessException("Authentication already exists");
        }
        else {
            auths.add(authData);
        }
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
}
