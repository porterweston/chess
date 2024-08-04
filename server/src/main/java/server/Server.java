package server;

import com.google.gson.JsonSyntaxException;
import dataaccess.interfaces.*;
import dataaccess.memory.MemoryAuthDAO;
import dataaccess.memory.MemoryGameDAO;
import dataaccess.memory.MemoryUserDAO;
import org.eclipse.jetty.server.Authentication;
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

    //Service instances
    GameService gameService = new GameService(new MemoryGameDAO(), new MemoryAuthDAO());
    UserService userService = new UserService(new MemoryAuthDAO(), new MemoryUserDAO());
    ClearService clearService = new ClearService(new MemoryUserDAO(), new MemoryGameDAO(), new MemoryAuthDAO());

    public Server() {
        registerHandler = new RegisterHandler(gameService, userService, clearService);
        loginHandler = new LoginHandler(gameService, userService, clearService);
        logoutHandler = new LogoutHandler(gameService, userService, clearService);
        listGamesHandler = new ListGamesHandler(gameService, userService, clearService);
        createGameHandler = new CreateGameHandler(gameService, userService, clearService);
        joinGameHandler = new JoinGameHandler(gameService, userService, clearService);
        clearHandler = new ClearHandler(gameService, userService, clearService);
        errorHandler = new ErrorHandler(gameService, userService, clearService);
    }

    public Server(GameService gameService, UserService userService, ClearService clearService) {
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
