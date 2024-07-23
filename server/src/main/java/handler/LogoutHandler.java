package handler;

import spark.*;
import model.*;
import service.*;

public class LogoutHandler extends Handler{
    public Object handleRequest(Request req, Response res) throws ErrorException {
        LogoutRequest request = new LogoutRequest(req.headers("authorization"));
        LogoutResult result = userService.logout(request);
        return toJson(result);
    }
}
