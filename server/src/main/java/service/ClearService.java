package service;
import model.*;
import dataaccess.*;

public class ClearService {

    MemoryUserDAO userDAO = new MemoryUserDAO();
    MemoryGameDAO gameDAO = new MemoryGameDAO();
    MemoryAuthDAO authDAO = new MemoryAuthDAO();

    public ClearResult clear(ClearRequest req) {
        userDAO.deleteUsers();
        gameDAO.deleteGames();
        authDAO.deleteAuths();
        return new ClearResult(true);
    }
}
