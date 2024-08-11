package handler;

import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WebSocketSessions {
    private static final Map<Integer, Set<Session>> SESSION_MAP = new HashMap<>();

    public void addSessionToGame(Integer gameID, Session session) {
        SESSION_MAP.computeIfAbsent(gameID, k -> new HashSet<Session>());
        Set<Session> sessionSet = SESSION_MAP.get(gameID);
        sessionSet.add(session);
        SESSION_MAP.put(gameID, sessionSet);
    }

    public void removeSessionFromGame(Integer gameID, Session session) {
        if (!(SESSION_MAP.get(gameID) == null)) {
            Set<Session> sessionSet = SESSION_MAP.get(gameID);
            sessionSet.remove(session);
            SESSION_MAP.put(gameID, sessionSet);
        }
    }

    public void removeSession(Session session) {
        for (Integer gameID : SESSION_MAP.keySet()) {
            Set<Session> sessionSet = SESSION_MAP.get(gameID);
            sessionSet.remove(session);
            SESSION_MAP.put(gameID, sessionSet);
        }
    }

    public Set<Session> getSessionsForGame(Integer gameID) {
        return SESSION_MAP.get(gameID);
    }
}
