package dataaccess;

import model.*;

import java.util.Collection;
import java.util.HashSet;

public class MemoryUserDAO implements UserDAO{

    private static Collection<UserData> users = new HashSet<UserData>();

    @Override
    public UserData getUser(String username) throws DataAccessException {
        for (UserData user : users) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        throw new DataAccessException("User does not exist");
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        if (users.contains(userData)) {
            throw new DataAccessException("User already exists");
        }
        else {
            users.add(userData);
        }
    }

    @Override
    public void deleteUsers() {
        users.clear();
    }
}
