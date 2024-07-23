package service;

import model.*;
import dataaccess.*;

public class UserService {

    private MemoryAuthDAO authDAO = new MemoryAuthDAO();
    private MemoryUserDAO userDAO = new MemoryUserDAO();

    public RegisterResult register(RegisterRequest req) throws ErrorException{
        //make new user
        try {
            UserData newUser = new UserData(req.username(), req.password(), req.email());
            //if any fields are null, bad request
            if (req.username() == null || req.password() == null || req.email() == null) {
                throw new ErrorException(400, "bad request");
            }
            userDAO.createUser(newUser);
        }
        catch (DataAccessException e) {
            //user with the same username and email already exists
            throw new ErrorException(403, "already taken");
        }
        //make new auth
        String authToken = authDAO.createAuth(req.username());
        return new RegisterResult(req.username(), authToken);
    }

    public LoginResult login(LoginRequest req) throws ErrorException{
        //get the user
        UserData user = userDAO.getUser(req.username());

        //ensure user exists and passwords match
        if (user == null || !user.password().equals(req.password())) {
            throw new ErrorException(401, "unauthorized");
        }

        //authenticate user
        String authToken = authDAO.createAuth(req.username());
        return new LoginResult(req.username(), authToken);
    }

    public LogoutResult logout(LogoutRequest req) throws ErrorException{
        try {
            //verify authorization
            AuthData auth = authDAO.getAuth(req.authToken());

            //delete authorization
            authDAO.deleteAuth(auth.authToken());

            return new LogoutResult();
        }
        catch (DataAccessException exception) {
            throw new ErrorException(401, "unauthorized");
        }
    }
}
