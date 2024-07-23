package handler;

import spark.*;
import model.*;
import service.*;
import com.google.gson.JsonObject;

public class CreateGameHandler extends Handler{
    public Object handleRequest(Request req, Response res) throws ErrorException {
        CreateGameRequest initialRequest = (CreateGameRequest) fromJson(req.body(), CreateGameRequest.class);
        String authToken = req.headers("authorization");
        CreateGameRequest request = new CreateGameRequest(authToken, initialRequest.gameName());
        CreateGameResult result = gameService.createGame(request);
        return toJson(result);
    }
}
