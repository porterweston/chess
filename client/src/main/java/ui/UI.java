package ui;


import facade.*;

public abstract class UI {
    public static ServerFacade facade;
    public static String authToken;

    public UI() {
        facade = new ServerFacade(8080);
        authToken = null;
    }

    public abstract String eval(String line);

    public String handleError(String errorCode) {
        return switch(errorCode) {
            case "401" -> "Error: unauthorized";
            case "403" -> "Error: already taken";
            default -> "Error: bad request";
        };
    }
}
