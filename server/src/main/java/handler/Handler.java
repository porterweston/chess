package handler;

import com.google.gson.*;
import service.*;

public abstract class Handler {
    //service instances
    public final GameService gameService;
    public final UserService userService;
    public final ClearService clearService;

    public Handler(GameService gameService, UserService userService, ClearService clearService) {
        this.gameService = gameService;
        this.userService = userService;
        this.clearService = clearService;
    }

    Gson serializer = new Gson();

    public Object toJson(Object obj) {
        return serializer.toJson(obj);
    }

    public <T> Object fromJson(Object obj, Class<T> c) {
        return serializer.fromJson((String)obj, c);
    }
}
