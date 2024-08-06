package facade;

import reqres.*;

import java.net.*;

public class ServerFacade {
    private final int serverPort;
    private final String serverURL;
    private final HttpURLConnection http;

    public ServerFacade(int port) throws Exception{
        this.serverPort = port;
        this.serverURL = "http://localhost";
        URI uri = new URI(String.format("%s:%i/", serverURL, serverPort));
        this.http = (HttpURLConnection) uri.toURL().openConnection();
    }

    public void clear() {}

    public RegisterResult register() {
        return null;
    }

    public LoginResult login() {
        return null;
    }

    public LogoutResult logout() {
        return null;
    }

    public ListGamesResult listGames() {
        return null;
    }

    public CreateGameResult createGame() {
        return null;
    }

    public JoinGameResult joinGame() {
        return null;
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) {

    }
}
