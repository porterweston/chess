package service;

import model.*;
import dataaccess.*;

import javax.xml.crypto.Data;

public class UserService {

    private MemoryAuthDAO authDAO = new MemoryAuthDAO();
    private MemoryUserDAO userDAO = new MemoryUserDAO();

    public RegisterResult register(RegisterRequest req) throws ErrorException{
        //make new user
        try {
            UserData newUser = new UserData(req.username(), req.password(), req.email());
            userDAO.createUser(newUser);
        }
        catch (DataAccessException e) {
            //user with the same username and email already exists
            throw new ErrorException(403, "already taken");
        }
        //make new auth
        try {
            String authToken = authDAO.createAuth(req.username());
            return new RegisterResult(req.username(), authToken);
        }
        catch (DataAccessException e) {
            throw new ErrorException(500, "already authenticated");
        }
    }

    public LoginResult login(LoginRequest req){
        return null;
    }

    public LogoutRequest logout(LogoutRequest req){
        return null;
    }
}
