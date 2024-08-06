package client;

import dataaccess.DataAccessException;
import org.junit.jupiter.api.*;
import server.Server;
import facade.*;
import reqres.*;
import dataaccess.mysql.*;
import model.*;

import java.util.HashSet;


public class ServerFacadeTests {

    private static Server server;
    private static ServerFacade facade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        facade = new ServerFacade(port);
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void clear() {
        try {
            facade.clear();
            //ensure database is empty
            MySQLUserDAO userDAO = new MySQLUserDAO();
            MySQLGameDAO gameDAO = new MySQLGameDAO();
            MySQLAuthDAO authDAO = new MySQLAuthDAO();
            Assertions.assertEquals(new HashSet<UserData>(), userDAO.getUsersDatabase());
            Assertions.assertEquals(new HashSet<GameData>(), gameDAO.getGamesDatabase());
            Assertions.assertEquals(new HashSet<AuthData>(), authDAO.getAuthsDatabase());
        } catch (Exception e) {
            return;
        }
    }

    @Test
    public void registerPositive() {
        try {
            var result = facade.register(new RegisterRequest("johndoe", "12345", "johndoe@email.com"));
            Assertions.assertEquals("johndoe", result.username());
            Assertions.assertTrue(result.authToken().length() > 10);
        } catch (ResponseException e) {
            return;
        }
    }

    @Test
    public void registerNegative() {
        try {
            var result = facade.register(new RegisterRequest(null, null, null));
        } catch (ResponseException e) {
            Assertions.assertEquals(e.errorCode, 500);
        }
    }

}
