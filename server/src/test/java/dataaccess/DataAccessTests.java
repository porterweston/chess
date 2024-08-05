package dataaccess;

import chess.ChessGame;
import dataaccess.mysql.*;
import model.*;

import org.junit.jupiter.api.*;
import org.mindrot.jbcrypt.BCrypt;

import java.util.ArrayList;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class DataAccessTests {
    //data access instances
    private static MySQLUserDAO userDAO;
    private static MySQLGameDAO gameDAO;
    private static MySQLAuthDAO authDAO;

    private static String currentAuthToken;
    private static int currentGameID;

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
    }

    @AfterAll
    public static void clearDatabase() {
        userDAO.deleteUsers();
        authDAO.deleteAuths();
        gameDAO.deleteGames();
    }

    /*
        USER TESTS
     */
    @Test
    @Order(1)
    public void createUserPositive() {
        clearUsers();
        try {
            userDAO.createUser(new UserData("johndoe", "12345", "johndoe@email.com"));
        } catch (DataAccessException e) {
            System.out.printf("Unable to create user: %s", e.getMessage());
        }
        getUserPositive();
    }

    @Test
    @Order(2)
    public void createUserNegative() {
        //try to create already existing user
        Assertions.assertThrows(DataAccessException.class, () -> userDAO.createUser(new UserData("johndoe",
                "12345", "johndoe@email.com")));
    }

    @Test
    @Order(3)
    public void getUserPositive() {
        UserData actualUser = userDAO.getUser("johndoe");
        Assertions.assertEquals("johndoe", actualUser.username());
        Assertions.assertTrue(BCrypt.checkpw("12345", actualUser.password()));
        Assertions.assertEquals("johndoe@email.com", actualUser.email());
    }

    @Test
    @Order(4)
    public void getUserNegative() {
        //try to get a non-existing user
        UserData actualUser = userDAO.getUser("doejohn");
        Assertions.assertNull(actualUser);
    }

    @Test
    @Order(5)
    public void clearUsers() {
        userDAO.deleteUsers();
        UserData actualUser = userDAO.getUser("johndoe");
        Assertions.assertNull(actualUser);
    }

    /*
        AUTH TESTS
     */
    @Test
    @Order(6)
    public void createAuthPositive() {
        createUserPositive();
        currentAuthToken = authDAO.createAuth("johndoe");
        getAuthPositive();
    }

    @Test
    @Order(7)
    public void createAuthNegative() {
        //try to create auth for null user
        String actualAuthToken = authDAO.createAuth(null);
        Assertions.assertNull(actualAuthToken);
    }

    @Test
    @Order(8)
    public void getAuthPositive() {
        try {
            AuthData actualAuth = authDAO.getAuth(currentAuthToken);
            AuthData expectedAuth = new AuthData(currentAuthToken, "johndoe");
            Assertions.assertEquals(expectedAuth, actualAuth);
        } catch (DataAccessException e) {
            return;
        }
    }

    @Test
    @Order(9)
    public void getAuthNegative() {
        //try to get non-existing auth
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.getAuth("12345"));
    }

    @Test
    @Order(10)
    public void deleteAuth() {
        try {
            authDAO.deleteAuth(currentAuthToken);
            Assertions.assertThrows(DataAccessException.class, () -> authDAO.getAuth(currentAuthToken));
        } catch (DataAccessException e) {
            return;
        }
    }

    @Test
    @Order(11)
    public void clearAuths() {
        authDAO.deleteAuths();
        Assertions.assertThrows(DataAccessException.class, () -> authDAO.getAuth(currentAuthToken));
    }

    /*
        GAME TESTS
     */
    @Test
    @Order(12)
    public void createGamePositive() {
        currentGameID = gameDAO.createGame("john's game");
        getGamePositive();
    }

    @Test
    @Order(13)
    public void createGameNegative() {
        //attempt to create a game with a null name
        int gameID = gameDAO.createGame(null);
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.getGame(gameID));
    }

    @Test
    @Order(14)
    public void getGamePositive() {
        try {
            GameData actualGame = gameDAO.getGame(currentGameID);
            GameData expectedGame = new GameData(currentGameID, null, null,
                    "john's game", new ChessGame());
            Assertions.assertEquals(expectedGame, actualGame);
        } catch (DataAccessException e) {
            return;
        }
    }

    @Test
    @Order(15)
    public void getGameNegative() {
        //attempt to get a non-existing game
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.getGame(-1));
    }

    @Test
    @Order(16)
    public void listGamesPositive() {
        //add another game
        gameDAO.createGame("doe's game");
        ArrayList<GameData> actualGames = (ArrayList<GameData>) gameDAO.listGames();
        var expectedGames = new ArrayList<>(){{
            add(new GameData(1, null, null,
                "john's game", new ChessGame()));
            add(new GameData(2, null, null,
                    "doe's game", new ChessGame()));}};
        Assertions.assertEquals(expectedGames, actualGames);
    }

    @Test
    @Order(17)
    public void listGamesNegative() {
        //try to list games when database is empty
        gameDAO.deleteGames();
        var games = gameDAO.listGames();
        Assertions.assertEquals(new ArrayList<>(), games);
    }

    @Test
    @Order(18)
    public void updateGamePositive() {
        currentGameID = gameDAO.createGame("john's game");
        try {
            GameData currentGame = gameDAO.getGame(currentGameID);
            GameData updatedGame = new GameData(currentGame.gameID(), "john", "doe",
                    currentGame.gameName(), currentGame.game());
            gameDAO.updateGame(currentGame, updatedGame);
        } catch (DataAccessException e) {
            return;
        }
    }

    @Test
    @Order(19)
    public void updateGameNegative() {
        //attempt to update a game with a null game name
        try {
            GameData currentGame = gameDAO.getGame(currentGameID);
            GameData updatedGame = new GameData(currentGame.gameID(), "john", "doe",
                    null, currentGame.game());
            Assertions.assertThrows(DataAccessException.class, () -> gameDAO.updateGame(currentGame, updatedGame));
        } catch (DataAccessException e) {
            return;
        }

    }

    @Test
    @Order(20)
    public void clearGames() {
        gameDAO.deleteGames();
        Assertions.assertThrows(DataAccessException.class, () -> gameDAO.getGame(1));
    }
}