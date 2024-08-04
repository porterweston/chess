package handler;

import reqres.ListGamesRequest;
import reqres.ListGamesResult;
import spark.*;
import service.*;

public class ListGamesHandler extends Handler{
    public ListGamesHandler(GameService gameService, UserService userService, ClearService clearService) {
        super(gameService, userService, clearService);
    }

    public Object handleRequest(Request req, Response res) throws ErrorException {
        ListGamesRequest request = new ListGamesRequest(req.headers("authorization"));
        ListGamesResult result = gameService.listGames(request);
        return toJson(result);
    }
}
