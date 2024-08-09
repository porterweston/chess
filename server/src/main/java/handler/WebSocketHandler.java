package handler;

import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.*;

@WebSocket
public class WebSocketHandler {
    private final WebSocketSessions sessions = new WebSocketSessions();

    @OnWebSocketConnect
    public void onConnect(Session session) {

    }

    @OnWebSocketClose
    public void onClose(Session session) {

    }

    @OnWebSocketError
    public void onError(Throwable throwable) {

    }

    @OnWebSocketMessage
    public void onMessage(Session session, String str) {
        //determine message type

    }

    private void connect(UserGameCommand command) {

    }

    private void makeMove(MakeMoveCommand command) {

    }

    private void leaveGame(UserGameCommand command) {

    }

    private void resignGame(UserGameCommand command) {

    }
}
