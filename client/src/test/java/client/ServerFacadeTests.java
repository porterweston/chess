package client;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import server.Server;
import facade.*;
import reqres.*;
import dataaccess.mysql.*;
import model.*;

import java.util.HashSet;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    private static String currentAuth;

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
            var result = facade.register(new RegisterRequest("johndoe", "12345", "johndoe@email.com"));
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
}
