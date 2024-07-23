package handler;

import spark.*;
import model.*;
import service.*;

public class LoginHandler extends Handler{
    public Object handleRequest(Request req, Response res) throws ErrorException {
        LoginRequest request = (LoginRequest)fromJson(req.body(), LoginRequest.class);
        LoginResult result = userService.login(request);
        return toJson(result);
    }
}
