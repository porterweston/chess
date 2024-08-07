package handler;

import service.ErrorException;
import reqres.LoginRequest;
import reqres.LoginResult;
import spark.*;
import service.*;

public class LoginHandler extends Handler{
    public LoginHandler(GameService gameService, UserService userService, ClearService clearService) {
        super(gameService, userService, clearService);
    }

    public Object handleRequest(Request req, Response res) throws ErrorException {
        LoginRequest request = (LoginRequest)fromJson(req.body(), LoginRequest.class);
        LoginResult result = userService.login(request);
        return toJson(result);
    }
}
