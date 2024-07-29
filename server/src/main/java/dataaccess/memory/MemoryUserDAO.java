package dataaccess.memory;

import dataaccess.DataAccessException;
import dataaccess.interfaces.UserDAO;
import model.*;

import java.util.Collection;
import java.util.HashSet;

public class MemoryUserDAO implements UserDAO {

    private static Collection<UserData> users = new HashSet<UserData>();

    @Override
    public UserData getUser(String username) {
        for (UserData user : users) {
            if (user.username().equals(username)) {
                return user;
            }
        }
        return null;
    }

    @Override
    public void createUser(UserData userData) throws DataAccessException {
        for (UserData user : users) {
            if (user.username().equals(userData.username())) {
                throw new DataAccessException("User already exists");
            }
        }
        users.add(userData);
    }

    @Override
    public void deleteUsers() {
        users.clear();
    }

    //returns what's in the database for testing purposes
    public Collection<UserData> getUsersDatabase() {
        return users;
    }
}
