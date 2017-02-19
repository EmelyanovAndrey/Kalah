package com.pesikovlike.kalah.game.session;

import com.pesikovlike.kalah.game.bid.GameBid;
import com.pesikovlike.kalah.model.entity.GameState;

import javax.websocket.Session;
import java.util.Map;

/**
 * Created by Igor on 06.12.2016.
 */
public interface GameSessionService {

    public GameSession addGameSession(GameBid bid);

    public GameSession getGameSession(String creatorLogin);

    public Map<String, GameSession> getAllGameSessions();

    public int deleteGameSession(String creatorLogin);

    public int makeStep(String creatorLogin, int player);

    public GameSession getGameSessionForLoad(long id);

    public GameSession addGameSession(String creatorLogin, GameState gameState, Session creatorSession, Session joinedSession);
}
