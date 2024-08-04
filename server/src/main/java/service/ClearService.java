package service;
import dataaccess.memory.*;
import dataaccess.interfaces.*;
import reqres.*;

public class ClearService {

    private final UserDAO userDAO;
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;

    public ClearService(UserDAO userDAO, GameDAO gameDAO, AuthDAO authDAO) {
        this.userDAO = userDAO;
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
    }

    public ClearResult clear(ClearRequest req) {
        userDAO.deleteUsers();
        gameDAO.deleteGames();
        authDAO.deleteAuths();
        return new ClearResult();
    }
}
