package client;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import server.Server;
import facade.*;
import reqres.*;
import dataaccess.mysql.*;
import model.*;
import chess.*;

import java.util.HashSet;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    private static String currentAuth;
    private static int currentGameID;

    //data access instances
    private static MySQLUserDAO userDAO;
    private static MySQLGameDAO gameDAO;
    private static MySQLAuthDAO authDAO;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
        try {
            facade.clear();
        } catch (ResponseException e) {
            System.out.printf("Unable to clear database: %s%n", e.getMessage());
        }
        try {
            userDAO = new MySQLUserDAO();
            gameDAO = new MySQLGameDAO();
            authDAO = new MySQLAuthDAO();
        } catch (DataAccessException e) {
            System.out.printf("Unable to initialize DAOs: %s%n", e.getMessage());
        }
    }

    @AfterAll
    static void stopServer() {
        try {
            facade.clear();
        } catch (ResponseException e) {
            System.out.printf("Unable to clear database: %s%n", e.getMessage());
        }
        server.stop();
    }


    @Test
    @Order(1)
    public void clear() {
        try {
            facade.clear();
            //ensure database is empty
            Assertions.assertTrue(userDAO.getUsersDatabase().isEmpty());
            Assertions.assertTrue(gameDAO.getGamesDatabase().isEmpty());
            Assertions.assertTrue(authDAO.getAuthsDatabase().isEmpty());
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(2)
    public void registerPositive() {
        try {
            var result = facade.register(new RegisterRequest("johndoe", "12345",
                    "johndoe@email.com"));
            currentAuth = result.authToken();
            Assertions.assertEquals("johndoe", result.username());
            Assertions.assertTrue(result.authToken().length() > 10);
        } catch (ResponseException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(3)
    public void registerNegative() {
        try {
            var result = facade.register(new RegisterRequest(null, null, null));
            Assertions.fail("Registered non-existing user");
        } catch (ResponseException e) {
            Assertions.assertEquals(e.errorCode, 500);
        }
    }

    @Test
    @Order(4)
    public void loginPositive() {
        try {
            var result = facade.login(new LoginRequest("johndoe", "12345"));
            Assertions.assertNotEquals(currentAuth, result.authToken());
            Assertions.assertTrue(result.authToken().length() > 10);
        } catch (ResponseException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(5)
    public void loginNegative() {
        try {
            var result = facade.login(new LoginRequest(null, null));
            Assertions.fail("Logged in non-existing user");
        } catch (ResponseException e) {
            Assertions.assertEquals(500, e.errorCode);
        }
    }

    @Test
    @Order(6)
    public void logoutPositive() {
        try {
            facade.logout(new LogoutRequest(currentAuth));
            Assertions.assertThrows(DataAccessException.class, () -> authDAO.getAuth(currentAuth));
        } catch (ResponseException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(7)
    public void logoutNegative() {
        try {
            facade.logout(new LogoutRequest(null));
            Assertions.fail("Logged out non-existing authToken");
        } catch (ResponseException e) {
            Assertions.assertEquals(500, e.errorCode);
        }
    }

    @Test
    @Order(8)
    public void createGamePositive() {
        try {
            var loginResult = facade.login(new LoginRequest("johndoe", "12345"));
            currentAuth = loginResult.authToken();
            var createGameResult = facade.createGame(new CreateGameRequest(currentAuth, "john's game"));
            currentGameID = createGameResult.gameID();
            GameData expectedGame = new GameData(currentGameID, null, null,
                    "john's game", new ChessGame());
            Assertions.assertEquals(expectedGame, gameDAO.getGame(createGameResult.gameID()));
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }

    }

    @Test
    @Order(9)
    public void createGameNegative() {
        try {
            facade.createGame(new CreateGameRequest(null, "john's bad game"));
            Assertions.fail("Created game with bad authorization");
        } catch (ResponseException e) {
            Assertions.assertEquals(500, e.errorCode);
        }
    }

    @Test
    @Order(10)
    public void listGamesPositive() {
        try {
            var result = facade.listGames(new ListGamesRequest(currentAuth));
            Assertions.assertEquals(1, result.games().size());
        } catch (ResponseException e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(11)
    public void listGamesNegative() {
        try {
            facade.listGames(new ListGamesRequest(null));
            Assertions.fail("Listed games with bad authorization");
        } catch (ResponseException e) {
            Assertions.assertEquals(500, e.errorCode);
        }
    }

    @Test
    @Order(12)
    public void joinGamePositive() {
        try {
            facade.joinGame(new JoinGameRequest(currentAuth, ChessGame.TeamColor.WHITE, currentGameID));
            GameData expectedGame = new GameData(currentGameID, "johndoe", null,
                    "john's game", new ChessGame());
            Assertions.assertEquals(expectedGame, gameDAO.getGame(currentGameID));
        } catch (Exception e) {
            Assertions.fail(e.getMessage());
        }
    }

    @Test
    @Order(13)
    public void joinGameNegative() {
        try {
            facade.joinGame(new JoinGameRequest(currentAuth, ChessGame.TeamColor.WHITE, currentGameID));
            Assertions.fail("Joined game twice as the same team color");
        } catch (ResponseException e) {
            Assertions.assertEquals(500, e.errorCode);
        }
    }
}
