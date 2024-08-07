package dataaccess.interfaces;
import dataaccess.DataAccessException;
import model.*;

public interface UserDAO {
    public UserData getUser(String username);
    public void createUser(UserData userData) throws DataAccessException;
    public void deleteUsers();
}
