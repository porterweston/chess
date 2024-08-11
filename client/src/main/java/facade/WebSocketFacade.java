package facade;

import javax.websocket.*;

import com.google.gson.Gson;
import ui.*;
import websocket.messages.*;
import websocket.commands.*;
import chess.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketFacade extends Endpoint {
    Session session;
    GameHandler gameHandler;

    public WebSocketFacade(Integer port, GameHandler gameHandler) throws ResponseException {
        try {
            String url = String.format("%s:%d", "ws://localhost", port);
            URI socketURI = new URI(String.format("%s%s", url, "/ws"));
            this.gameHandler = gameHandler;

            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            this.session = container.connectToServer(this, socketURI);

            this.session.addMessageHandler(new MessageHandler.Whole<String>() {
                @Override
                public void onMessage(String message) {
                    ServerMessage notification = new Gson().fromJson(message, ServerMessage.class);
                    switch (notification.getServerMessageType()) {
                        case LOAD_GAME -> gameHandler.updateGame(
                                new Gson().fromJson(message, LoadGameMessage.class).getGame());
                        case NOTIFICATION -> gameHandler.printMessage(
                                new Gson().fromJson(message, NotificationMessage.class).getMessage());
                        case ERROR -> gameHandler.printMessage(
                                new Gson().fromJson(message, ErrorMessage.class).getErrorMessage());
                    }
                }
            });
        } catch (DeploymentException | IOException | URISyntaxException e) {
            throw new ResponseException(400, e.getMessage());
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    public void connect(String authToken, int gameID) throws ResponseException {
        var command = new UserGameCommand(UserGameCommand.CommandType.CONNECT, authToken, gameID);
        sendCommand(command);
    }

    public void makeMove(String authToken, int gameID, ChessMove move) throws ResponseException{
        var command = new MakeMoveCommand(move, authToken, gameID);
        try {
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new ResponseException(400, e.getMessage());
        }
    }

    public void leaveGame(String authToken, int gameID) throws ResponseException{
        var command = new UserGameCommand(UserGameCommand.CommandType.LEAVE, authToken, gameID);
        sendCommand(command);
    }

    public void resignGame(String authToken, int gameID) throws ResponseException{
        var command = new UserGameCommand(UserGameCommand.CommandType.RESIGN, authToken, gameID);
        sendCommand(command);
    }

    private void sendCommand(UserGameCommand command) throws ResponseException{
        try {
            this.session.getBasicRemote().sendText(new Gson().toJson(command));
        } catch (IOException e) {
            throw new ResponseException(400, e.getMessage());
        }
    }
}
