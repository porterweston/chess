package handler;

import reqres.*;
import spark.*;
import service.*;

public class CreateGameHandler extends Handler{
    public CreateGameHandler(GameService gameService, UserService userService, ClearService clearService) {
        super(gameService, userService, clearService);
    }

    public Object handleRequest(Request req, Response res) throws ErrorException {
        CreateGameRequest initialRequest = (CreateGameRequest) fromJson(req.body(), CreateGameRequest.class);
        String authToken = req.headers("authorization");
        CreateGameRequest request = new CreateGameRequest(authToken, initialRequest.gameName());
        CreateGameResult result = gameService.createGame(request);
        return toJson(result);
    }
}
