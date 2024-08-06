package facade;

import com.google.gson.Gson;
import reqres.*;

import java.io.*;
import java.net.*;

public class ServerFacade {
    private final int serverPort;
    private final String serverURL;

    public ServerFacade(int port) throws Exception{
        this.serverPort = port;
        this.serverURL = "http://localhost";
    }

    public void clear() {
        String path = "/clear";
    }

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

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException{
        try {
            URI uri = new URI(String.format("%s:%d%s", serverURL, serverPort, path));
            HttpURLConnection http = (HttpURLConnection) uri.toURL().openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception e) {
            throw new ResponseException(500, e.getMessage());
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

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        //if status code isn't successful
        if (!(status / 100 == 2)) {
            throw new ResponseException(status, "failure: " + status);
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
}
