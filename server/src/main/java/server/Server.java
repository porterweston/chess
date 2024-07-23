package server;

import com.google.gson.JsonSyntaxException;
import spark.*;
import handler.*;
import service.*;

public class Server {

    //Handler instances
    private final RegisterHandler registerHandler = new RegisterHandler();
    private final LoginHandler loginHandler = new LoginHandler();
    private final LogoutHandler logoutHandler = new LogoutHandler();
    private final ListGamesHandler listGamesHandler = new ListGamesHandler();
    private final CreateGameHandler createGameHandler = new CreateGameHandler();
    private final JoinGameHandler joinGameHandler = new JoinGameHandler();
    private final ClearHandler clearHandler = new ClearHandler();
    private final ErrorHandler errorHandler = new ErrorHandler();

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
