package com.pesikovlike.kalah.game.session;

import com.pesikovlike.kalah.model.entity.GameState;

import javax.websocket.Session;

/**
 * Created by Igor on 06.12.2016.
 */
public interface GameSession {
    public Session getSessionOfCreator();

    public void setSessionOfCreator(Session sessionOfCreator);

    public Session getSessionOfJoined();

    public void setSessionOfJoined(Session sessionOfJoined);

    public GameState getGameState();

    public void setGameState(GameState gameState);
}
