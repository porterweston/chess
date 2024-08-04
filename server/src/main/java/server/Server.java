package server;

import com.google.gson.JsonSyntaxException;
import spark.*;
import handler.*;
import service.*;

public class Server {

    //Handler instances
    private RegisterHandler registerHandler;
    private LoginHandler loginHandler;
    private LogoutHandler logoutHandler;
    private ListGamesHandler listGamesHandler;
    private CreateGameHandler createGameHandler;
    private JoinGameHandler joinGameHandler;
    private ClearHandler clearHandler;
    private ErrorHandler errorHandler;

    //Service instances
    private final GameService gameService;
    private final UserService userService;
    private final ClearService clearService;

    public Server(GameService gameService, UserService userService, ClearService clearService) {
        //initialize services
        this.gameService = gameService;
        this.userService = userService;
        this.clearService = clearService;

        //initialize handlers
        registerHandler = new RegisterHandler();
        loginHandler = new LoginHandler();
        logoutHandler = new LogoutHandler();
        listGamesHandler = new ListGamesHandler();
        createGameHandler = new CreateGameHandler();
        joinGameHandler = new JoinGameHandler();
        clearHandler = new ClearHandler(gameService, userService, clearService);
        errorHandler = new ErrorHandler();
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
