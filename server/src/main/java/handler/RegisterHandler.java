package handler;

import reqres.RegisterRequest;
import reqres.RegisterResult;
import spark.*;
import service.*;

public class RegisterHandler extends Handler{
    public Object handleRequest(Request req, Response res) throws ErrorException {
        RegisterRequest request = (RegisterRequest)fromJson(req.body(), RegisterRequest.class);
        RegisterResult result = userService.register(request);
        return toJson(result);
    }
}
