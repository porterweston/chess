package server;

import com.google.gson.JsonSyntaxException;
import dataaccess.DataAccessException;
import dataaccess.mysql.*;
import dataaccess.memory.*;
import spark.*;
import handler.*;
import service.*;

public class Server {

    //Handler instances
    private final RegisterHandler registerHandler;
    private final LoginHandler loginHandler;
    private final LogoutHandler logoutHandler;
    private final ListGamesHandler listGamesHandler;
    private final CreateGameHandler createGameHandler;
    private final JoinGameHandler joinGameHandler;
    private final ClearHandler clearHandler;
    private final ErrorHandler errorHandler;

    public Server() {
        //initialize databases
        MySQLUserDAO userDAO = null;
        MySQLAuthDAO authDAO = null;
        MySQLGameDAO gameDAO = null;
        try {
            userDAO = new MySQLUserDAO();
            authDAO = new MySQLAuthDAO();
            gameDAO = new MySQLGameDAO();
        } catch (DataAccessException e) {
            System.out.printf("Unable to initialize databases: %s", e.getMessage());
        }

        //initialize services
        GameService gameService = new GameService(gameDAO, authDAO);
        UserService userService = new UserService(authDAO, userDAO);
        ClearService clearService = new ClearService(userDAO, gameDAO, authDAO);

        //initialize handlers
        registerHandler = new RegisterHandler(gameService, userService, clearService);
        loginHandler = new LoginHandler(gameService, userService, clearService);
        logoutHandler = new LogoutHandler(gameService, userService, clearService);
        listGamesHandler = new ListGamesHandler(gameService, userService, clearService);
        createGameHandler = new CreateGameHandler(gameService, userService, clearService);
        joinGameHandler = new JoinGameHandler(gameService, userService, clearService);
        clearHandler = new ClearHandler(gameService, userService, clearService);
        errorHandler = new ErrorHandler(gameService, userService, clearService);
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.
        Spark.post("/user", registerHandler::handleRequest);
        Spark.post("/session", loginHandler::handleRequest);
        Spark.delete("/session", logoutHandler::handleRequest);
        Spark.get("/game", listGamesHandler::handleRequest);
        Spark.post("/game", createGameHandler::handleRequest);
        Spark.put("/game", joinGameHandler::handleRequest);
        Spark.delete("/db", clearHandler::handleRequest);
        Spark.exception(ErrorException.class, errorHandler::handleErrorException);
        Spark.exception(JsonSyntaxException.class, errorHandler::handleJSONSyntaxException);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
