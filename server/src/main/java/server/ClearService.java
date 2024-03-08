package server;

import model.*;
import dataAccess.*;

public class ClearService {
    //Singleton pattern
    private static ClearService clearService = new ClearService();
    private ClearService(){}
    public static ClearService getInstance(){
        return clearService;
    }

    //calls all clear methods in all DAOs
    public void clearApplication() {
        var authDAO = MemoryAuthDAO.getInstance();
        authDAO.clearAuths();
        var userDAO = MemoryUserDAO.getInstance();
        userDAO.clearUsers();
        var gameDAO = MemoryGameDAO.getInstance();
        gameDAO.clearGames();
    }
}
