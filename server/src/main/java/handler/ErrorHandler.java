package handler;

import com.google.gson.JsonSyntaxException;
import spark.*;
import model.*;
import service.*;

public class ErrorHandler extends Handler{
    public void handleErrorException(ErrorException e, Request req, Response res) {
        res.status(e.errorCode);
        res.body((String)toJson(new Error(e.getMessage())));
    }

    public void handleJSONSyntaxException(JsonSyntaxException e, Request req, Response res) {
        res.status(400);
        res.body((String)toJson(new Error("Error: bad request")));
    }
}
