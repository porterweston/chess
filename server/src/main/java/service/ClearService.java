package service;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryGameDAO;
import dataaccess.memory.MemoryUserDAO;
import reqres.ClearRequest;
import reqres.ClearResult;

public class ClearService {

    MemoryUserDAO userDAO = new MemoryUserDAO();
    MemoryGameDAO gameDAO = new MemoryGameDAO();
    MemoryAuthDAO authDAO = new MemoryAuthDAO();

    public ClearResult clear(ClearRequest req) {
        userDAO.deleteUsers();
        gameDAO.deleteGames();
        authDAO.deleteAuths();
        return new ClearResult();
    }
}
