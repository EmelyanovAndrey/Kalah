package com.pesikovlike.kalah.game;

import com.pesikovlike.kalah.model.entity.GameState;

import javax.ejb.Singleton;
import javax.websocket.Session;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Created by Igor on 07.11.2016.
 */
public class GameSessionsService {

    private final Map<Long, GameSession> gameSessions = new HashMap<Long, GameSession>();


    public int addGameSession(GameSession gameSession){
        gameSessions.put(gameSession.getId(), gameSession);

        return 0;
    }

    public GameSession getGameSession(Long id) {
        return gameSessions.get(id);
    }

    public Map<Long, GameSession> getAllGameSessions() {
        return gameSessions;
    }

    public int deleteGameSession(Long id) {
        gameSessions.remove(id);
        return 0;
    }
}
