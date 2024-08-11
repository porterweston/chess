package handler;

import org.eclipse.jetty.websocket.api.Session;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class WebSocketSessions {
    private static final Map<Integer, Set<Session>> sessionMap = new HashMap<>();

    public void addSessionToGame(Integer gameID, Session session) {
        sessionMap.computeIfAbsent(gameID, k -> new HashSet<Session>());
        Set<Session> sessionSet = sessionMap.get(gameID);
        sessionSet.add(session);
        sessionMap.put(gameID, sessionSet);
    }

    public void removeSessionFromGame(Integer gameID, Session session) {
        if (!(sessionMap.get(gameID) == null)) {
            Set<Session> sessionSet = sessionMap.get(gameID);
            sessionSet.remove(session);
            sessionMap.put(gameID, sessionSet);
        }
    }

    public void removeSession(Session session) {
        for (Integer gameID : sessionMap.keySet()) {
            Set<Session> sessionSet = sessionMap.get(gameID);
            sessionSet.remove(session);
            sessionMap.put(gameID, sessionSet);
        }
    }

    public Set<Session> getSessionsForGame(Integer gameID) {
        return sessionMap.get(gameID);
    }
}
