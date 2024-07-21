package service;
import dataaccess.MemoryGameDAO;
import model.*;
import dataaccess.*;
import chess.*;

import org.junit.jupiter.api.*;
import java.util.Collection;
import java.util.HashSet;

public class ServiceTests {

    //DAOs
    private MemoryGameDAO gameDAO = new MemoryGameDAO();
    private MemoryUserDAO userDAO = new MemoryUserDAO();
    private MemoryAuthDAO authDAO = new MemoryAuthDAO();

    //Services
    private GameService gameService = new GameService();
    private UserService userService = new UserService();
    private ClearService clearService = new ClearService();

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

        try {
            //register a new user
            RegisterResult result = userService.register(new RegisterRequest("JohnDoe", "12345", "johndoe@email.com"));

            //ensure user was added to database
            Assertions.assertEquals(new HashSet<UserData>(){{add(new UserData("JohnDoe", "12345", "johndoe@email.com"));}}, userDAO.getUsersDatabase());
            Assertions.assertEquals(new HashSet<AuthData>(){{add(new AuthData(result.authToken(), "JohnDoe"));}}, authDAO.getAuthsDatabase());
        }
        catch (ErrorException exception) {
            return;
        }


    }

    @Test
    public void RegisterTestNegative() {
        clearDatabase();

        try {
            //register a new user
            userService.register(new RegisterRequest("JohnDoe", "12345", "johndoe@email.com"));

            try {
                //register another user with the same username and email
                userService.register(new RegisterRequest("JohnDoe", "54321", "johndoe@email.com"));
            }
            catch (ErrorException exception) {
                Assertions.assertEquals(403, exception.errorCode);
            }
        }
        catch (ErrorException exception) {
            return;
        }


    }

    @Test
    public void LoginTestPositive() {
        clearDatabase();

        try {
            //register a new user
            RegisterResult registerResult = userService.register(new RegisterRequest("JohnDoe", "12345", "johndoe@email.com"));

            try {
                //logout user
                userService.logout(new LogoutRequest(registerResult.authToken()));

                //login user
                try {
                    LoginResult loginResult = userService.login(new LoginRequest("JohnDoe", "12345"));
                    Assertions.assertEquals(new HashSet<UserData>(){{add(new UserData("JohnDoe", "12345", "johndoe@email.com"));}}, userDAO.getUsersDatabase());
                    Assertions.assertEquals(new HashSet<AuthData>(){{add(new AuthData(loginResult.authToken(), "JohnDoe"));}}, authDAO.getAuthsDatabase());
                }
                catch (ErrorException exception) {
                    return;
                }
            }
            catch (ErrorException exception) {
                return;
            }
        }
        catch (ErrorException exception) {
            return;
        }
    }

    @Test
    public void LoginTestNegative() {
        clearDatabase();

        //login a non-existing user
        try {
            userService.login(new LoginRequest("JohnDoe", "12345"));
        }
        catch (ErrorException exception) {
            Assertions.assertEquals(401, exception.errorCode);
        }
    }

    @Test
    public void LogoutTestPositive() {
        clearDatabase();

        try {
            //register a new user
            RegisterResult result = userService.register(new RegisterRequest("JohnDoe", "12345", "johndoe@email.com"));

            try {
                //logout user
                userService.logout(new LogoutRequest(result.authToken()));
                Assertions.assertEquals(new HashSet<UserData>(){{add(new UserData("JohnDoe", "12345", "johndoe@email.com"));}}, userDAO.getUsersDatabase());
                Assertions.assertEquals(new HashSet<AuthData>(), authDAO.getAuthsDatabase());
            }
            catch (ErrorException exception) {
                return;
            }
        }
        catch (ErrorException exception) {
            return;
        }
    }

    @Test
    public void LogoutTestNegative() {
        clearDatabase();

        //logout non-existing user
        try {
            userService.logout(new LogoutRequest("12345"));
        }
        catch (ErrorException exception) {
            Assertions.assertEquals(401, exception.errorCode);
        }
    }

    @Test
    public void ListGamesTestPositive() {
        clearDatabase();

        try {
            //register a new user
            RegisterResult registerResult = userService.register(new RegisterRequest("JohnDoe", "12345", "johndoe@email.com"));

            try {
                //create a new game
                CreateGameResult createGameResult = gameService.createGame(new CreateGameRequest(registerResult.authToken(), "John's Game"));

                try {
                    //list games
                    ListGamesResult listGamesResult = gameService.listGames(new ListGamesRequest(registerResult.authToken()));
                    Assertions.assertEquals(new HashSet<GameData>(){{add(new GameData(createGameResult.gameID(), null, null, "John's Game", new ChessGame()));}}, listGamesResult.games());
                }
                catch (ErrorException exception) {
                    return;
                }
            }
            catch (ErrorException exception) {
                return;
            }
        }
        catch (ErrorException exception) {
            return;
        }
    }

    @Test
    public void ListGamesTestNegative() {
        clearDatabase();

        try {
            gameService.listGames(new ListGamesRequest("12345"));
        }
        catch (ErrorException exception) {
            Assertions.assertEquals(401, exception.errorCode);
        }
        //try listing games without being authenticated
    }

    @Test
    public void CreateGameTestPositive() {
        clearDatabase();

        try {
            //register a new user
            RegisterResult registerResult = userService.register(new RegisterRequest("JohnDoe", "12345", "johndoe@email.com"));

            try {
                //create a new game
                CreateGameResult createGameResult = gameService.createGame(new CreateGameRequest(registerResult.authToken(), "John's Game"));

                Assertions.assertEquals(new HashSet<GameData>(){{add(new GameData(createGameResult.gameID(), null, null, "John's Game", new ChessGame()));}}, gameDAO.getGamesDatabase());
            }
            catch (ErrorException exception) {
                return;
            }
        }
        catch (ErrorException exception) {
            return;
        }
    }

    @Test
    public void CreateGameTestNegative() {
        clearDatabase();

        //try creating a game without being authorized
        try {
            gameService.createGame(new CreateGameRequest("12345", "John's Game"));
        }
        catch (ErrorException exception) {
            Assertions.assertEquals(401, exception.errorCode);
        }
    }

    private void clearDatabase() {
        clearService.clear(new ClearRequest());
    }
}
