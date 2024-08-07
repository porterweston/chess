package facade;

import chess.ChessGame;
import com.google.gson.Gson;
import reqres.*;

import java.io.*;
import java.net.*;
import java.util.Base64;

public class ServerFacade {
    private final int serverPort;
    private final String serverURL;

    public ServerFacade(int port){
        this.serverPort = port;
        this.serverURL = "http://localhost";
    }

    public void clear() throws ResponseException{
        String path = "/db";
        makeRequest("DELETE", path, null, null, null);
    }

    public RegisterResult register(RegisterRequest req) throws ResponseException{
        String path = "/user";
        return makeRequest("POST", path, null, req, RegisterResult.class);
    }

    public LoginResult login(LoginRequest req) throws ResponseException{
        String path = "/session";
        return makeRequest("POST", path, null, req, LoginResult.class);
    }

    public void logout(LogoutRequest req) throws ResponseException{
        String path = "/session";
        makeRequest("DELETE", path, req.authToken(), null, null);
    }

    public ListGamesResult listGames(ListGamesRequest req) throws ResponseException{
        String path = "/game";
        return makeRequest("GET", path, req.authToken(), null, ListGamesResult.class);
    }

    public CreateGameResult createGame(CreateGameRequest req) throws ResponseException{
        String path = "/game";
        record bodyRequest(String gameName) {};
        return makeRequest("POST", path, req.authToken(), new bodyRequest(req.gameName()),
                CreateGameResult.class);
    }

    public void joinGame(JoinGameRequest req) throws ResponseException{
        String path = "/game";
        record bodyRequest(ChessGame.TeamColor playerColor, int gameID) {};
        makeRequest("PUT", path, req.authToken(), new bodyRequest(req.playerColor(), req.gameID()),
                null);
    }

    private <T> T makeRequest(String method, String path, String headerRequest,
                              Object bodyRequest, Class<T> responseClass) throws ResponseException{
        try {
            URI uri = new URI(String.format("%s:%d%s", serverURL, serverPort, path));
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeHeader(headerRequest, http);
            writeBody(bodyRequest, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        //if status code isn't successful
        if (!(status / 100 == 2)) {
            throw new ResponseException(status, "failure: " + status);
        }
    }

    private void writeHeader(String request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Authorization", request);
        }
    }

    private void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    public boolean isConnected() {
        try {
            URI uri = new URI(String.format("%s:%d", serverURL, serverPort));
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.connect();
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}
