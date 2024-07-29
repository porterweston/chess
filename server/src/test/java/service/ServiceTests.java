package service;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryGameDAO;
import dataaccess.memory.MemoryUserDAO;
import model.*;
import chess.*;

import org.junit.jupiter.api.*;
import reqres.*;

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
            RegisterResult result = userService.register(new RegisterRequest(
                    "JohnDoe", "12345", "johndoe@email.com"));

            //ensure user was added to database
            Assertions.assertEquals(new HashSet<UserData>(){{add(new UserData(
                    "JohnDoe", "12345", "johndoe@email.com"));}},
                    userDAO.getUsersDatabase());
            Assertions.assertEquals(new HashSet<AuthData>(){{add(new AuthData(
                    result.authToken(), "JohnDoe"));}}, authDAO.getAuthsDatabase());
        }
        catch (ErrorException exception) {
            return;
        }


    }

    @Test
    public void registerTestNegative() {
        clearDatabase();

        try {
            //register a new user
            userService.register(new RegisterRequest(
                    "JohnDoe", "12345", "johndoe@email.com"));

            try {
                //register another user with the same username and email
                userService.register(new RegisterRequest(
                        "JohnDoe", "54321", "johndoe@email.com"));
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
    public void loginTestPositive() {
        clearDatabase();

        try {
            //register a new user
            RegisterResult registerResult = userService.register(new RegisterRequest(
                    "JohnDoe", "12345", "johndoe@email.com"));

            try {
                //logout user
                userService.logout(new LogoutRequest(registerResult.authToken()));

                //login user
                try {
                    LoginResult loginResult = userService.login(new LoginRequest(
                            "JohnDoe", "12345"));
                    Assertions.assertEquals(new HashSet<UserData>(){{add(new UserData(
                            "JohnDoe", "12345", "johndoe@email.com"));}},
                            userDAO.getUsersDatabase());
                    Assertions.assertEquals(new HashSet<AuthData>(){{add(new AuthData(
                            loginResult.authToken(), "JohnDoe"));}}, authDAO.getAuthsDatabase());
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
    public void loginTestNegative() {
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
    public void logoutTestPositive() {
        clearDatabase();

        try {
            //register a new user
            RegisterResult result = userService.register(new RegisterRequest(
                    "JohnDoe", "12345", "johndoe@email.com"));

            try {
                //logout user
                userService.logout(new LogoutRequest(result.authToken()));
                Assertions.assertEquals(new HashSet<UserData>(){{add(new UserData(
                        "JohnDoe", "12345", "johndoe@email.com"));}},
                        userDAO.getUsersDatabase());
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
    public void logoutTestNegative() {
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
    public void listGamesTestPositive() {
        clearDatabase();

        try {
            //register a new user
            RegisterResult registerResult = userService.register(new RegisterRequest(
                    "JohnDoe", "12345", "johndoe@email.com"));

            try {
                //create a new game
                CreateGameResult createGameResult = gameService.createGame(new CreateGameRequest(
                        registerResult.authToken(), "John's Game"));

                try {
                    //list games
                    ListGamesResult listGamesResult = gameService.listGames(new ListGamesRequest(
                            registerResult.authToken()));
                    Assertions.assertEquals(new HashSet<GameData>(){{add(new GameData(
                            createGameResult.gameID(), null, null,
                            "John's Game", new ChessGame()));}}, listGamesResult.games());
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
    public void listGamesTestNegative() {
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
    public void createGameTestPositive() {
        clearDatabase();

        try {
            //register a new user
            RegisterResult registerResult = userService.register(new RegisterRequest(
                    "JohnDoe", "12345", "johndoe@email.com"));

            try {
                //create a new game
                CreateGameResult createGameResult = gameService.createGame(new CreateGameRequest(
                        registerResult.authToken(), "John's Game"));

                Assertions.assertEquals(new HashSet<GameData>(){{add(new GameData(
                        createGameResult.gameID(), null, null,
                        "John's Game", new ChessGame()));}}, gameDAO.getGamesDatabase());
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
    public void createGameTestNegative() {
        clearDatabase();

        //try creating a game without being authorized
        try {
            gameService.createGame(new CreateGameRequest("12345", "John's Game"));
        }
        catch (ErrorException exception) {
            Assertions.assertEquals(401, exception.errorCode);
        }
    }

    @Test
    public void joinGameTestPositive() {
        clearDatabase();

        try {
            //register a new user
            RegisterResult registerResult = userService.register(new RegisterRequest(
                    "JohnDoe", "12345", "johndoe@email.com"));

            try {
                //create a new game
                CreateGameResult createGameResult = gameService.createGame(new CreateGameRequest(
                        registerResult.authToken(), "John's Game"));

                try {
                    //join game
                    gameService.joinGame(new JoinGameRequest(
                            registerResult.authToken(), ChessGame.TeamColor.WHITE, createGameResult.gameID()));

                    Assertions.assertEquals(new HashSet<GameData>(){{add(new GameData(
                            createGameResult.gameID(), "JohnDoe", null,
                            "John's Game", new ChessGame()));}}, gameDAO.getGamesDatabase());
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
    public void joinGameTestNegative() {
        clearDatabase();

        CreateGameResult createGameResult;

        try {
            //register a new user
            RegisterResult registerResult = userService.register(new RegisterRequest(
                    "JohnDoe", "12345", "johndoe@email.com"));

            try {
                //create a new game
                createGameResult = gameService.createGame(new CreateGameRequest(
                        registerResult.authToken(), "John's Game"));

                try {
                    //join game
                    gameService.joinGame(new JoinGameRequest(
                            registerResult.authToken(), ChessGame.TeamColor.WHITE, createGameResult.gameID()));
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

        try {
            //register another new user
            RegisterResult registerResult2 = userService.register(new RegisterRequest(
                    "DoeJohn", "54321", "doejohn@email.com"));
            try {
                //try joining the same game
                gameService.joinGame(new JoinGameRequest(
                        registerResult2.authToken(), ChessGame.TeamColor.WHITE, createGameResult.gameID()));
            }
            catch (ErrorException exception) {
                Assertions.assertEquals(403, exception.errorCode);
            }
        }
        catch (ErrorException exception) {
            return;
        }
    }

    private void clearDatabase() {
        clearService.clear(new ClearRequest());
    }
}
