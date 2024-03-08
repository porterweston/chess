package dataAccess;

import model.UserData;

import java.sql.Array;
import java.util.ArrayList;

public class MemoryUserDAO implements UserDAO{
    //Singleton pattern
    private static MemoryUserDAO userDAO = new MemoryUserDAO();
    private MemoryUserDAO(){}
    public static MemoryUserDAO getInstance() {
        return userDAO;
    }

    //data
    private ArrayList<UserData> users = new ArrayList<UserData>();

    //methods
    @Override
    public UserData getUser(String username){
        return null;
    }
    @Override
    public void createUser(String username, String password, String email){

    }
    @Override
    public void clearUsers(){
        this.users.clear();
    }
}
