package handler;

import com.google.gson.*;
import service.*;

public abstract class Handler {
    //service instances
    UserService userService = new UserService();
    GameService gameService = new GameService();
    ClearService clearService = new ClearService();

    Gson serializer = new Gson();

    public Object toJson(Object obj) {
        return serializer.toJson(obj);
    }

    public <T> Object fromJson(Object obj, Class<T> c) {
        return serializer.fromJson((String)obj, c);
    }
}
