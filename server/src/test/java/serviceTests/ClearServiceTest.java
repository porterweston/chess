package serviceTests;

import org.junit.jupiter.api.Test;
import server.*;
import dataAccess.*;

public class ClearServiceTest {
    @Test
    void clearApplicationTest(){
        ClearService clearService = ClearService.getInstance();
        clearService.clearApplication();
        //DAOs
        var authDAO = MemoryAuthDAO.getInstance();
        var gameDAO = MemoryGameDAO.getInstance();
        var userDAO = MemoryUserDAO.getInstance();
        //data
        var auths = authDAO.getAllAuths();
        var games = gameDAO.getAllGames();
        var users = userDAO.getAllUsers();
        assert auths.isEmpty() && games.isEmpty() && users.isEmpty() : "Application not cleared";
    }
}
