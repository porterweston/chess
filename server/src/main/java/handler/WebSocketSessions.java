package handler;

import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WebSocketSessions {
    private static final Map<Integer, Set<Session>> SessionMap = new HashMap<>();

    public void addSessionToGame(Integer gameID, Session session) {
        SessionMap.computeIfAbsent(gameID, k -> new HashSet<Session>());
        Set<Session> sessionSet = SessionMap.get(gameID);
        sessionSet.add(session);
        SessionMap.put(gameID, sessionSet);
    }

    public void removeSessionFromGame(Integer gameID, Session session) {
        if (!(SessionMap.get(gameID) == null)) {
            Set<Session> sessionSet = SessionMap.get(gameID);
            sessionSet.remove(session);
            SessionMap.put(gameID, sessionSet);
        }
    }

    public void removeSession(Session session) {
        for (Integer gameID : SessionMap.keySet()) {
            Set<Session> sessionSet = SessionMap.get(gameID);
            sessionSet.remove(session);
            SessionMap.put(gameID, sessionSet);
        }
    }

    public Set<Session> getSessionsForGame(Integer gameID) {
        return SessionMap.get(gameID);
    }
}
