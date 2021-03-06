package com.pesikovlike.kalah.game.session.implementation;

import com.pesikovlike.kalah.game.session.GameSession;
import com.pesikovlike.kalah.model.entity.GameState;

import javax.websocket.Session;

/**
 * Created by Igor on 09.11.2016.
 */
public class GameSessionImpl implements GameSession {
    private Session sessionOfCreator;
    private Session sessionOfJoined;
    private GameState gameState;


    public GameSessionImpl() {}
    public GameSessionImpl(Session sessionOfCreator, Session sessionOfJoined, GameState gameState){
        this.sessionOfCreator = sessionOfCreator;
        this.sessionOfJoined = sessionOfJoined;
        this.gameState = gameState;
    }

    public Session getSessionOfCreator() {
        return sessionOfCreator;
    }

    public void setSessionOfCreator(Session sessionOfCreator) {
        this.sessionOfCreator = sessionOfCreator;
    }

    public Session getSessionOfJoined() {
        return sessionOfJoined;
    }

    public void setSessionOfJoined(Session sessionOfJoined) {
        this.sessionOfJoined = sessionOfJoined;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

}
