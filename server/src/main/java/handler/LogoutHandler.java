package handler;

import reqres.LogoutRequest;
import reqres.LogoutResult;
import spark.*;
import service.*;

public class LogoutHandler extends Handler{
    public LogoutHandler(GameService gameService, UserService userService, ClearService clearService) {
        super(gameService, userService, clearService);
    }

    public Object handleRequest(Request req, Response res) throws ErrorException {
        LogoutRequest request = new LogoutRequest(req.headers("authorization"));
        LogoutResult result = userService.logout(request);
        return toJson(result);
    }
}
