package service;

import dataaccess.*;
import dataaccess.mysql.*;
import model.*;
import chess.*;

import org.junit.jupiter.api.*;
import reqres.*;

import java.util.ArrayList;
import java.util.HashSet;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServiceTests {

    //data access instances
    private static MySQLGameDAO gameDAO;
    private static MySQLUserDAO userDAO;
    private static MySQLAuthDAO authDAO;

    //service instances
    private static GameService gameService;
    private static UserService userService;
    private static ClearService clearService;

    @BeforeAll
    public static void init() {
        try {
            userDAO = new MySQLUserDAO();
            gameDAO = new MySQLGameDAO();
            authDAO = new MySQLAuthDAO();
            userDAO.deleteUsers();
            gameDAO.deleteGames();
            authDAO.deleteAuths();
        }
        catch (DataAccessException e) {
            System.out.printf("Unable to initialize database: %s", e.getMessage());
        }

        gameService = new GameService(gameDAO, authDAO);
        userService = new UserService(authDAO, userDAO);
        clearService = new ClearService(userDAO, gameDAO, authDAO);
    }

    @AfterAll
    public static void clearDatabase() {
        clearService.clear(new ClearRequest());
    }

    @Test
    @Order(1)
    public void clearTest() {
        clearDatabase();

        Assertions.assertEquals(new HashSet<GameData>(), gameDAO.getGamesDatabase());
        Assertions.assertEquals(new HashSet<UserData>(), userDAO.getUsersDatabase());
        Assertions.assertEquals(new HashSet<AuthData>(), authDAO.getAuthsDatabase());
    }

    @Test
    @Order(2)
    public void registerTestPositive() {
        clearDatabase();

        try {
            //register a new user
            RegisterResult result = userService.register(new RegisterRequest(
                    "JohnDoe", "12345", "johndoe@email.com"));

            //ensure user was added to database
            UserData actualUser = userDAO.getUser("JohnDoe");
            Assertions.assertEquals("JohnDoe", actualUser.username());
            Assertions.assertEquals(new HashSet<AuthData>(){{add(new AuthData(
                    result.authToken(), "JohnDoe"));}}, authDAO.getAuthsDatabase());
        }
        catch (ErrorException exception) {
            return;
        }


    }

    @Test
    @Order(3)
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
    @Order(4)
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
                    UserData actualUser = userDAO.getUser("JohnDoe");
                    Assertions.assertEquals("JohnDoe", actualUser.username());
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
    @Order(5)
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
    @Order(6)
    public void logoutTestPositive() {
        clearDatabase();

        try {
            //register a new user
            RegisterResult result = userService.register(new RegisterRequest(
                    "JohnDoe", "12345", "johndoe@email.com"));

            try {
                //logout user
                userService.logout(new LogoutRequest(result.authToken()));
                UserData actualUser = userDAO.getUser("JohnDoe");
                Assertions.assertEquals("JohnDoe", actualUser.username());
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
    @Order(7)
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
    @Order(8)
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
                    Assertions.assertEquals(new ArrayList<GameData>(){{add(new GameData(
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
    @Order(9)
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
    @Order(10)
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
    @Order(11)
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
    @Order(12)
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
    @Order(13)
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
}
