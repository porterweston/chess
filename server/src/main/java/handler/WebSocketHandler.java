package handler;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.Session;
import websocket.commands.*;
import service.*;
import websocket.messages.*;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@WebSocket
public class WebSocketHandler {
    private final WebSocketSessions sessions = new WebSocketSessions();
    private final WebSocketService service = new WebSocketService();

    @OnWebSocketError
    public void onError(Session session, Throwable t) throws ErrorException{
        throw new ErrorException(400, t.getMessage());
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String command) throws ErrorException{
        //deserialize command
        UserGameCommand c = new Gson().fromJson(command, UserGameCommand.class);
        //determine command type
        switch (c.getCommandType()) {
            case UserGameCommand.CommandType.CONNECT -> connect(session, c);
            case UserGameCommand.CommandType.MAKE_MOVE -> makeMove(session, command);
            case UserGameCommand.CommandType.LEAVE -> leaveGame(session, c);
            case UserGameCommand.CommandType.RESIGN -> resignGame(session, c);
        }
    }

    private void connect(Session session, UserGameCommand command) throws ErrorException{
        sessions.addSessionToGame(command.getGameID(), session);
        var messages = service.connect(command.getAuthToken(), command.getGameID());
        sendMessage(messages[0], session);
        sendBroadcast(command.getGameID(), messages[1], session);
    }

    private void makeMove(Session session, String c) throws ErrorException{
        MakeMoveCommand command = new Gson().fromJson(c, MakeMoveCommand.class);
        var messages = service.makeMove(command.getAuthToken(), command.getGameID(), command.getMove());
        if (messages[0].getServerMessageType() == ServerMessage.ServerMessageType.ERROR) {
            sendMessage(messages[0], session);
            return;
        }
        sendBroadcast(command.getGameID(), messages[0], null);
        sendBroadcast(command.getGameID(), messages[1], session);
        //move resulted in check/checkmate/stalemate
        if (messages[2] != null) {
            sendBroadcast(command.getGameID(), messages[2], null);
        }
    }

    private void leaveGame(Session session, UserGameCommand command) throws ErrorException{
        sessions.removeSessionFromGame(command.getGameID(), session);
        var message = service.leaveGame(command.getAuthToken(), command.getGameID());
        sendBroadcast(command.getGameID(), message, session);
    }

    private void resignGame(Session session, UserGameCommand command) throws ErrorException{
        var message = service.resignGame(command.getAuthToken(), command.getGameID());
        if (!(message.getServerMessageType() == ServerMessage.ServerMessageType.ERROR)) {
            sendBroadcast(command.getGameID(), message, null);
        }
        else {
            sendMessage(message, session);
        }
    }

    //sends a message object to a given session
    private void sendMessage(ServerMessage message, Session session) throws ErrorException{
        String str = new Gson().toJson(message);
        try {
            session.getRemote().sendString(str);
        } catch (IOException e) {
            throw new ErrorException(400, "Unable to send message to client");
        }
    }

    //sends a message to all sessions excluding the given session
    private void sendBroadcast(Integer gameID, ServerMessage message, Session exceptThisSession) throws ErrorException{
        var removeList = new HashSet<Session>();
        Set<Session> sessionSet = sessions.getSessionsForGame(gameID);
        for (Session s : sessionSet) {
            if (s.isOpen()) {
                if (exceptThisSession == null) {
                    sendMessage(message, s);
                }
                else if (!s.equals(exceptThisSession)) {
                    sendMessage(message, s);
                }
            }
            else {
                removeList.add(s);
            }
        }

        for (Session s : removeList) {
            sessions.removeSession(s);
        }
    }
}
