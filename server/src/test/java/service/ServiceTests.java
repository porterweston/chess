package service;
import dataaccess.MemoryGameDAO;
import model.*;
import dataaccess.*;

import org.junit.jupiter.api.*;
import java.util.Collection;
import java.util.HashSet;

public class ServiceTests {

    //DAOs
    private MemoryGameDAO gameDAO = new MemoryGameDAO();
    private MemoryUserDAO userDAO = new MemoryUserDAO();
    private MemoryAuthDAO authDAO = new MemoryAuthDAO();

    @Test
    public void clearTest() {
        clearDatabase();

        Assertions.assertEquals(new HashSet<GameData>(), gameDAO.getGamesDatabase());
        Assertions.assertEquals(new HashSet<UserData>(), userDAO.getUsersDatabase());
        Assertions.assertEquals(new HashSet<AuthData>(), authDAO.getAuthsDatabase());
    }

    @Test
    public void registerTestPositive() {
        clearDatabase();

        //register a new user
        UserService userService = new UserService();
        try {
            userService.register(new RegisterRequest("JohnDoe", "12345", "johndoe@email.com"));
        }
        catch (ErrorException exception) {
            return;
        }

        //ensure user was added to database
        Collection<UserData> expectedUserData = new HashSet<>();
        expectedUserData.add(new UserData("JohnDoe", "12345", "johndoe@email.com"));
        Assertions.assertEquals(expectedUserData, userDAO.getUsersDatabase());

        //ensure user was authenticated
        Collection<AuthData> authData = authDAO.getAuthsDatabase();
        for (AuthData auth : authData) {
            Assertions.assertEquals("JohnDoe", auth.username());
        }
    }

    @Test
    public void RegisterTestNegative() {
        clearDatabase();

        //register a new user
        UserService userService = new UserService();
        try {
            userService.register(new RegisterRequest("JohnDoe", "12345", "johndoe@email.com"));
        }
        catch (ErrorException exception) {
            return;
        }

        //register another user with the same username and email
        try {
            userService.register(new RegisterRequest("JohnDoe", "54321", "johndoe@email.com"));
        }
        catch (ErrorException exception) {
            Assertions.assertEquals(403, exception.errorCode);
        }
    }

    private void clearDatabase() {
        ClearService clearService = new ClearService();
        clearService.clear(new ClearRequest());
    }
}
