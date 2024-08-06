package client;

import org.junit.jupiter.api.*;
import server.Server;
import facade.*;
import reqres.*;


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
    public void sampleTest() {
        Assertions.assertTrue(true);
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
