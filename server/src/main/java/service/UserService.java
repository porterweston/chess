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

        //ensure user exists
        if (user == null) {
            throw new ErrorException(401, "unauthorized");
        }

        //verify passwords match
        if (!user.password().equals(req.password())) {
            throw new ErrorException(500, "incorrect password");
        }

        //make new auth
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
