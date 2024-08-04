package handler;

import reqres.JoinGameRequest;
import reqres.JoinGameResult;
import spark.*;
import service.*;

public class JoinGameHandler extends Handler{
    public JoinGameHandler(GameService gameService, UserService userService, ClearService clearService) {
        super(gameService, userService, clearService);
    }

    public Object handleRequest(Request req, Response res) throws ErrorException {
        JoinGameRequest initialRequest = (JoinGameRequest)fromJson(req.body(), JoinGameRequest.class);
        String authToken = req.headers("authorization");
        JoinGameRequest request = new JoinGameRequest(authToken, initialRequest.playerColor(), initialRequest.gameID());
        JoinGameResult result = gameService.joinGame(request);
        return toJson(result);
    }
}
