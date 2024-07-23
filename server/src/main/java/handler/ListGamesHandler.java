package handler;

import spark.*;
import model.*;
import service.*;

public class ListGamesHandler extends Handler{
    public Object handleRequest(Request req, Response res) throws ErrorException {
        ListGamesRequest request = new ListGamesRequest(req.headers("authorization"));
        ListGamesResult result = gameService.listGames(request);
        return toJson(result);
    }
}
